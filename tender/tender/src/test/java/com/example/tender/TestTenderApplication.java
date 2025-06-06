package com.example.tender;

import org.springframework.boot.SpringApplication;

public class TestTenderApplication {

	public static void main(String[] args) {
		SpringApplication.from(TenderApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
