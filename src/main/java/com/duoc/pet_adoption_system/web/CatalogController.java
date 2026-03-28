package com.duoc.pet_adoption_system.web;

import com.duoc.pet_adoption_system.pets.application.GetPetByIdUseCase;
import com.duoc.pet_adoption_system.pets.application.ListAllPetsUseCase;
import com.duoc.pet_adoption_system.pets.application.SearchPetsUseCase;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.web.dto.PetView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogController {

	private final ListAllPetsUseCase listAllPetsUseCase;
	private final SearchPetsUseCase searchPetsUseCase;
	private final GetPetByIdUseCase getPetByIdUseCase;

	public CatalogController(
			ListAllPetsUseCase listAllPetsUseCase,
			SearchPetsUseCase searchPetsUseCase,
			GetPetByIdUseCase getPetByIdUseCase) {
		this.listAllPetsUseCase = listAllPetsUseCase;
		this.searchPetsUseCase = searchPetsUseCase;
		this.getPetByIdUseCase = getPetByIdUseCase;
	}

	@GetMapping("/catalog")
	public String catalog(
			@RequestParam(required = false) String species,
			@RequestParam(required = false) Integer age,
			@RequestParam(required = false) String location,
			@RequestParam(required = false) PetGender gender,
			Model model) {
		boolean hasCriteria = (species != null && !species.isBlank())
				|| age != null
				|| (location != null && !location.isBlank())
				|| gender != null;
		List<PetView> pets = (hasCriteria
				? searchPetsUseCase.execute(species, age, location, gender)
				: listAllPetsUseCase.execute())
				.stream()
				.map(PetView::from)
				.toList();
		model.addAttribute("pets", pets);
		model.addAttribute("species", species == null ? "" : species);
		model.addAttribute("age", age);
		model.addAttribute("location", location == null ? "" : location);
		model.addAttribute("gender", gender);
		return "catalog/list";
	}

	@GetMapping("/catalog/{id}")
	public String detail(@PathVariable String id, Model model) {
		model.addAttribute("pet", PetView.from(getPetByIdUseCase.execute(id)));
		return "catalog/detail";
	}
}
