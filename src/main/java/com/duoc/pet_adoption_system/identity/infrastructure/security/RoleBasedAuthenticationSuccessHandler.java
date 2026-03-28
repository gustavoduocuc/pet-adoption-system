package com.duoc.pet_adoption_system.identity.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException {
		boolean staffOrAdmin = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(a -> "ROLE_ADMIN".equals(a) || "ROLE_STAFF".equals(a));
		if (staffOrAdmin) {
			response.sendRedirect(request.getContextPath() + "/app/pets");
		}
		else {
			response.sendRedirect(request.getContextPath() + "/catalog");
		}
	}
}
