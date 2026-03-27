package com.duoc.pet_adoption_system.shared.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class DotenvConfigurer {

	private DotenvConfigurer() {
	}

	public static void apply() {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();
		dotenv.entries().forEach(entry -> {
			String key = entry.getKey();
			if (System.getenv(key) != null || System.getProperty(key) != null) {
				return;
			}
			System.setProperty(key, entry.getValue());
		});
	}
}
