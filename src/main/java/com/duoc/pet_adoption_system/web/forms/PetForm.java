package com.duoc.pet_adoption_system.web.forms;

import com.duoc.pet_adoption_system.pets.domain.entities.AdoptionStatus;
import com.duoc.pet_adoption_system.pets.domain.entities.PetGender;
import com.duoc.pet_adoption_system.pets.domain.entities.PetSpecies;

public class PetForm {

	private String name = "";
	private PetSpecies species;
	private String breed = "";
	private int age;
	private String location = "";
	private PetGender gender;
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
