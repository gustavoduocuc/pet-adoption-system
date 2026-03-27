package com.duoc.pet_adoption_system;

import com.duoc.pet_adoption_system.shared.infrastructure.config.DotenvConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetAdoptionSystemApplication {

	public static void main(String[] args) {
		DotenvConfigurer.apply();
		SpringApplication.run(PetAdoptionSystemApplication.class, args);
	}

}
