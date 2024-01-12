package com.openclassrooms.projet3;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.openclassrooms.projet3.entites")
public class Projet3Application {

	public static void main(String[] args) {
		PropertyConfigurator.configure("projet3/src/main/resources/log4j.properties");
		SpringApplication.run(Projet3Application.class, args);
	}

}
