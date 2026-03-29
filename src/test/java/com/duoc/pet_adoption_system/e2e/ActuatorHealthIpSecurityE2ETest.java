package com.duoc.pet_adoption_system.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ActuatorHealthIpSecurityE2ETest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void healthOkWhenRemoteAddressIsLoopback() throws Exception {
		mockMvc.perform(get("/actuator/health")
						.with(request -> {
							request.setRemoteAddr("127.0.0.1");
							return request;
						}))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("status")));
	}

	@Test
	void healthForbiddenWhenRemoteAddressIsNotAllowed() throws Exception {
		mockMvc.perform(get("/actuator/health")
						.with(request -> {
							request.setRemoteAddr("198.51.100.1");
							return request;
						}))
				.andExpect(status().isForbidden());
	}
}
