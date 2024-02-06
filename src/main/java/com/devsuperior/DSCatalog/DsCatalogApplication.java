package com.devsuperior.DSCatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DsCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsCatalogApplication.class, args);
	}
}
