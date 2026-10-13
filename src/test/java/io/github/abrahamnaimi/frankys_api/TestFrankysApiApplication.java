package io.github.abrahamnaimi.frankys_api;

import org.springframework.boot.SpringApplication;

public class TestFrankysApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(FrankysApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
