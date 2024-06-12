package com.mapfre.mifel.vida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.mapfre.mifel.vida.entity", "com.mapfre.mifel.vida.model", "com.mapfre.mifel.vida.repository", 
//		"com.mapfre.mifel.vida.service", "com.mapfre.mifel.vida.config", "com.mapfre.mifel.vida.exception", "com.mapfre.mifel.vida.helper"
//		,"com.mapfre.mifel.vida.swagger", "com.mapfre.mifel.vida.utils", "com.mapfre.mifel.vida.mapper"})
//@EnableJpaRepositories(basePackages = "com.mapfre.mifel.vida.repository")
public class ApiRestLifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestLifeApplication.class, args);
	}

}
