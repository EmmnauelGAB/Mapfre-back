package com.mapfre.mifel.vida.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//@EnableWebMvc
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors
						.basePackage("com.mapfre.mifel.vida.controller"))
				.paths(PathSelectors.regex("/.*"))
				.build().apiInfo(apiEndPointsInfo());
	}
	
	
	public ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("API REST MIFEL VIDA")
				.description("Api rest de emision")
				.contact(new Contact("Mapfre", "Mapfre", "email@mapfre"))
				.license("Apache 2.0")
				.version("1.0.0")
				.build();
	}
	
}
