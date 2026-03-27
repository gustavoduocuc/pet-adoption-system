package com.duoc.pet_adoption_system.e2e;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAndPetsApiE2ETest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void loginReturnsBearerToken() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andExpect(jsonPath("$.tokenType").value("Bearer"));
	}

	@Test
	void loginWithBadPasswordReturnsUnauthorized() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void listPetsIsPublic() throws Exception {
		mockMvc.perform(get("/api/pets"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void createPetWithoutTokenReturnsUnauthorized() throws Exception {
		mockMvc.perform(post("/api/pets")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Luna","species":"dog","breed":"mix","age":2,"location":"Santiago","gender":"FEMALE","adoptionStatus":"AVAILABLE"}
								"""))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void createPetWithStaffTokenSucceeds() throws Exception {
		String body = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"staff\",\"password\":\"staff123\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String token = JsonPath.read(body, "$.accessToken");

		mockMvc.perform(post("/api/pets")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Luna","species":"dog","breed":"mix","age":2,"location":"Santiago","gender":"FEMALE","adoptionStatus":"AVAILABLE"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Luna"));
	}

	@Test
	void patientsForbiddenForUserRole() throws Exception {
		String body = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"user\",\"password\":\"user123\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String token = JsonPath.read(body, "$.accessToken");

		mockMvc.perform(get("/api/patients")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}
}
