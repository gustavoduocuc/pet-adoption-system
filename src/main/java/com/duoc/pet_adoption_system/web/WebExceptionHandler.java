package com.duoc.pet_adoption_system.web;

import com.duoc.pet_adoption_system.shared.domain.DomainError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackages = "com.duoc.pet_adoption_system.web")
public class WebExceptionHandler {

	@ExceptionHandler(DomainError.class)
	public String handleDomainError(
			DomainError error,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", error.getMessage());
		String uri = request.getRequestURI();
		if (uri.contains("/app/pets")) {
			return "redirect:/app/pets";
		}
		if (uri.contains("/app/patients")) {
			return "redirect:/app/patients";
		}
		return "redirect:/catalog";
	}
}
