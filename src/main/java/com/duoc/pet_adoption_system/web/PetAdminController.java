package com.duoc.pet_adoption_system.web;

import com.duoc.pet_adoption_system.pets.application.CreatePetUseCase;
import com.duoc.pet_adoption_system.pets.application.DeletePetUseCase;
import com.duoc.pet_adoption_system.pets.application.GetPetByIdUseCase;
import com.duoc.pet_adoption_system.pets.application.ListAllPetsUseCase;
import com.duoc.pet_adoption_system.pets.application.UpdatePetUseCase;
import com.duoc.pet_adoption_system.web.dto.PetView;
import com.duoc.pet_adoption_system.web.forms.PetForm;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/pets")
@Validated
public class PetAdminController {

	private final ListAllPetsUseCase listAllPetsUseCase;
	private final CreatePetUseCase createPetUseCase;
	private final GetPetByIdUseCase getPetByIdUseCase;
	private final UpdatePetUseCase updatePetUseCase;
	private final DeletePetUseCase deletePetUseCase;

	public PetAdminController(
			ListAllPetsUseCase listAllPetsUseCase,
			CreatePetUseCase createPetUseCase,
			GetPetByIdUseCase getPetByIdUseCase,
			UpdatePetUseCase updatePetUseCase,
			DeletePetUseCase deletePetUseCase) {
		this.listAllPetsUseCase = listAllPetsUseCase;
		this.createPetUseCase = createPetUseCase;
		this.getPetByIdUseCase = getPetByIdUseCase;
		this.updatePetUseCase = updatePetUseCase;
		this.deletePetUseCase = deletePetUseCase;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("pets", listAllPetsUseCase.execute().stream().map(PetView::from).toList());
		return "app/pets/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("petForm", new PetForm());
		model.addAttribute("editMode", false);
		return "app/pets/form";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute PetForm petForm, RedirectAttributes redirectAttributes) {
		if (!isPetFormValid(petForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Complete todos los campos obligatorios.");
			return "redirect:/app/pets/new";
		}
		var pet = createPetUseCase.execute(
				petForm.getName(),
				petForm.getSpecies(),
				petForm.getBreed(),
				petForm.getAge(),
				petForm.getLocation(),
				petForm.getGender(),
				petForm.getAdoptionStatus());
		redirectAttributes.addFlashAttribute("successMessage", "Mascota creada.");
		return "redirect:/app/pets/" + pet.id().value() + "/edit";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable @Size(max = 36) String id, Model model) {
		var pet = getPetByIdUseCase.execute(id);
		PetForm form = new PetForm();
		form.setName(pet.name());
		form.setSpecies(pet.species());
		form.setBreed(pet.breed());
		form.setAge(pet.age());
		form.setLocation(pet.location());
		form.setGender(pet.gender());
		form.setAdoptionStatus(pet.adoptionStatus());
		model.addAttribute("petForm", form);
		model.addAttribute("petId", id);
		model.addAttribute("editMode", true);
		return "app/pets/form";
	}

	@PostMapping("/{id}/edit")
	public String update(
			@PathVariable @Size(max = 36) String id,
			@ModelAttribute PetForm petForm,
			RedirectAttributes redirectAttributes) {
		if (!isPetFormValid(petForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Complete todos los campos obligatorios.");
			return "redirect:/app/pets/" + id + "/edit";
		}
		updatePetUseCase.execute(
				id,
				petForm.getName(),
				petForm.getSpecies(),
				petForm.getBreed(),
				petForm.getAge(),
				petForm.getLocation(),
				petForm.getGender(),
				petForm.getAdoptionStatus());
		redirectAttributes.addFlashAttribute("successMessage", "Mascota actualizada.");
		return "redirect:/app/pets";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable @Size(max = 36) String id, RedirectAttributes redirectAttributes) {
		deletePetUseCase.execute(id);
		redirectAttributes.addFlashAttribute("successMessage", "Mascota eliminada.");
		return "redirect:/app/pets";
	}

	private static boolean isPetFormValid(PetForm f) {
		return f.getName() != null && !f.getName().isBlank()
				&& f.getSpecies() != null
				&& f.getLocation() != null && !f.getLocation().isBlank()
				&& f.getGender() != null
				&& f.getAdoptionStatus() != null;
	}
}
