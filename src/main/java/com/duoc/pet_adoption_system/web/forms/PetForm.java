package com.duoc.pet_adoption_system.web.forms;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PetForm {

	@NotBlank
	@Size(max = 120)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
	private String name = "";

	@NotNull
	private PetSpecies species;

	@Size(max = 120)
	@Pattern(regexp = "^$|^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
	private String breed = "";

	@Min(0)
	@Max(500)
	private int age;

	@NotBlank
	@Size(max = 200)
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Solo letras, números y espacios")
	private String location = "";

	@NotNull
	private PetGender gender;

	@NotNull
	private AdoptionStatus adoptionStatus;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PetSpecies getSpecies() {
		return species;
	}

	public void setSpecies(PetSpecies species) {
		this.species = species;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public PetGender getGender() {
		return gender;
	}

	public void setGender(PetGender gender) {
		this.gender = gender;
	}

	public AdoptionStatus getAdoptionStatus() {
		return adoptionStatus;
	}

	public void setAdoptionStatus(AdoptionStatus adoptionStatus) {
		this.adoptionStatus = adoptionStatus;
	}
}
