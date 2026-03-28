package com.duoc.pet_adoption_system.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebThymeleafFlowE2ETest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void catalogIsPublicAndShowsTitle() throws Exception {
		mockMvc.perform(get("/catalog"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Catálogo de mascotas")));
	}

	@Test
	void appPetsRedirectsToLoginWhenAnonymous() throws Exception {
		mockMvc.perform(get("/app/pets"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	void staffFormLoginAllowsAppPets() throws Exception {
		MockHttpSession session = new MockHttpSession();
		mockMvc.perform(post("/login")
						.session(session)
						.param("username", "staff")
						.param("password", "staff123")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/app/pets"));

		mockMvc.perform(get("/app/pets").session(session))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Mascotas")));
	}

	@Test
	void userCannotAccessAppPets() throws Exception {
		MockHttpSession session = new MockHttpSession();
		mockMvc.perform(post("/login")
						.session(session)
						.param("username", "user")
						.param("password", "user123")
						.with(csrf()))
				.andExpect(redirectedUrl("/catalog"));

		mockMvc.perform(get("/app/pets").session(session))
				.andExpect(status().isForbidden());
	}

	@Test
	void staffCanOpenPatientsList() throws Exception {
		MockHttpSession session = new MockHttpSession();
		mockMvc.perform(post("/login")
						.session(session)
						.param("username", "staff")
						.param("password", "staff123")
						.with(csrf()))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(get("/app/patients").session(session))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Pacientes veterinarios")));
	}

	@Test
	void staffCanCreatePetViaWebForm() throws Exception {
		MockHttpSession session = new MockHttpSession();
		mockMvc.perform(post("/login")
						.session(session)
						.param("username", "staff")
						.param("password", "staff123")
						.with(csrf()))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(post("/app/pets/new")
						.session(session)
						.param("name", "WebPet")
						.param("species", "cat")
						.param("breed", "siames")
						.param("age", "1")
						.param("location", "Santiago")
						.param("gender", "FEMALE")
						.param("adoptionStatus", "AVAILABLE")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(result -> {
					String url = result.getResponse().getRedirectedUrl();
					if (url == null || !url.matches(".*/app/pets/.+/edit")) {
						throw new AssertionError("Expected redirect to /app/pets/{id}/edit, got: " + url);
					}
				});
	}
}
