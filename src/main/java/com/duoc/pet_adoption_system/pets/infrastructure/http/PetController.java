package com.duoc.pet_adoption_system.pets.infrastructure.http;

import com.duoc.pet_adoption_system.pets.application.CreatePetUseCase;
import com.duoc.pet_adoption_system.pets.application.DeletePetUseCase;
import com.duoc.pet_adoption_system.pets.application.GetPetByIdUseCase;
import com.duoc.pet_adoption_system.pets.application.ListAllPetsUseCase;
import com.duoc.pet_adoption_system.pets.application.ListAvailablePetsUseCase;
import com.duoc.pet_adoption_system.pets.application.SearchPetsUseCase;
import com.duoc.pet_adoption_system.pets.application.UpdatePetUseCase;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@Validated
public class PetController {

	private final ListAllPetsUseCase listAllPetsUseCase;
	private final ListAvailablePetsUseCase listAvailablePetsUseCase;
	private final SearchPetsUseCase searchPetsUseCase;
	private final GetPetByIdUseCase getPetByIdUseCase;
	private final CreatePetUseCase createPetUseCase;
	private final UpdatePetUseCase updatePetUseCase;
	private final DeletePetUseCase deletePetUseCase;

	public PetController(
			ListAllPetsUseCase listAllPetsUseCase,
			ListAvailablePetsUseCase listAvailablePetsUseCase,
			SearchPetsUseCase searchPetsUseCase,
			GetPetByIdUseCase getPetByIdUseCase,
			CreatePetUseCase createPetUseCase,
			UpdatePetUseCase updatePetUseCase,
			DeletePetUseCase deletePetUseCase) {
		this.listAllPetsUseCase = listAllPetsUseCase;
		this.listAvailablePetsUseCase = listAvailablePetsUseCase;
		this.searchPetsUseCase = searchPetsUseCase;
		this.getPetByIdUseCase = getPetByIdUseCase;
		this.createPetUseCase = createPetUseCase;
		this.updatePetUseCase = updatePetUseCase;
		this.deletePetUseCase = deletePetUseCase;
	}

	@GetMapping
	public List<PetResponse> listAll() {
		return listAllPetsUseCase.execute().stream().map(PetResponse::from).toList();
	}

	@GetMapping("/available")
	public List<PetResponse> listAvailable() {
		return listAvailablePetsUseCase.execute().stream().map(PetResponse::from).toList();
	}

	@GetMapping("/search")
	public List<PetResponse> search(
			@RequestParam(required = false) PetSpecies species,
			@RequestParam(required = false) Integer age,
			@RequestParam(required = false) @Size(max = 300) String location,
			@RequestParam(required = false) PetGender gender) {
		return searchPetsUseCase.execute(species, age, location, gender).stream()
				.map(PetResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public PetResponse getById(@PathVariable @Size(max = 36) String id) {
		return PetResponse.from(getPetByIdUseCase.execute(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PetResponse create(@Valid @RequestBody CreatePetRequest request) {
		var pet = createPetUseCase.execute(
				request.name(),
				request.species(),
				request.breed(),
				request.age(),
				request.location(),
				request.gender(),
				request.adoptionStatus());
		return PetResponse.from(pet);
	}

	@PutMapping("/{id}")
	public PetResponse update(@PathVariable @Size(max = 36) String id, @Valid @RequestBody UpdatePetRequest request) {
		var pet = updatePetUseCase.execute(
				id,
				request.name(),
				request.species(),
				request.breed(),
				request.age(),
				request.location(),
				request.gender(),
				request.adoptionStatus());
		return PetResponse.from(pet);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable @Size(max = 36) String id) {
		deletePetUseCase.execute(id);
	}
}
