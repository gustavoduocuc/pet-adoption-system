package com.duoc.pet_adoption_system.shared.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "app.security.actuator-health")
public class AppActuatorHealthSecurityProperties {

	private String allowedIps = "127.0.0.1,0:0:0:0:0:0:0:1";

	public String getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(String allowedIps) {
		this.allowedIps = allowedIps;
	}

	public List<String> resolveAllowedIpAddresses() {
		if (allowedIps == null || allowedIps.isBlank()) {
			return List.of();
		}
		return Arrays.stream(allowedIps.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();
	}
}
