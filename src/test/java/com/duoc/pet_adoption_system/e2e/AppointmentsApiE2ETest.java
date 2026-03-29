package com.duoc.pet_adoption_system.e2e;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentsApiE2ETest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void staffCreatesPatientAppointmentListsAndUpdates() throws Exception {
		String token = loginStaff();

		String patientBody = """
				{"name":"E2E Patient","species":"dog","intakeDate":"2026-03-01","treatmentNotes":null,"careStatus":"UNDER_CARE"}
				""";
		String patientResponse = mockMvc.perform(post("/api/patients")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(patientBody))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String patientId = JsonPath.read(patientResponse, "$.id");

		String createAppointmentBody = """
				{"scheduledAt":"2026-04-01T14:30:00","notes":"Checkup","status":"SCHEDULED"}
				""";
		String appointmentResponse = mockMvc.perform(post("/api/appointments/new/" + patientId)
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(createAppointmentBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.patientId").value(patientId))
				.andExpect(jsonPath("$.status").value("SCHEDULED"))
				.andExpect(jsonPath("$.createdByUsername").value("staff"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		String appointmentId = JsonPath.read(appointmentResponse, "$.id");

		mockMvc.perform(get("/api/appointments")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].id", hasItem(appointmentId)));

		mockMvc.perform(get("/api/appointments/" + patientId)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].id", hasItem(appointmentId)));

		String updateBody = """
				{"scheduledAt":"2026-04-02T09:00:00","notes":"Follow-up","status":"COMPLETED"}
				""";
		mockMvc.perform(put("/api/appointments/update/" + appointmentId)
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("COMPLETED"))
				.andExpect(jsonPath("$.notes").value("Follow-up"));
	}

	@Test
	void listAllAppointmentsForbiddenForUser() throws Exception {
		String token = loginUser();
		mockMvc.perform(get("/api/appointments")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	void listByPatientForbiddenForUser() throws Exception {
		String token = loginUser();
		mockMvc.perform(get("/api/appointments/00000000-0000-0000-0000-000000000001")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	void myAppointmentsOkForAuthenticatedUser() throws Exception {
		String token = loginUser();
		mockMvc.perform(get("/api/appointments/my")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void createAppointmentWithoutTokenReturnsUnauthorized() throws Exception {
		mockMvc.perform(post("/api/appointments/new/00000000-0000-0000-0000-000000000001")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"scheduledAt\":\"2026-04-01T10:00:00\",\"notes\":null,\"status\":\"SCHEDULED\"}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void createAppointmentForbiddenForUser() throws Exception {
		String token = loginUser();
		mockMvc.perform(post("/api/appointments/new/00000000-0000-0000-0000-000000000001")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"scheduledAt\":\"2026-04-01T10:00:00\",\"notes\":null,\"status\":\"SCHEDULED\"}"))
				.andExpect(status().isForbidden());
	}

	private String loginStaff() throws Exception {
		return login("staff", "staff123");
	}

	private String loginUser() throws Exception {
		return login("user", "user123");
	}

	private String login(String username, String password) throws Exception {
		String body = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		return JsonPath.read(body, "$.accessToken");
	}
}
