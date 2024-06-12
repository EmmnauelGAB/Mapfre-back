package com.mapfre.mifel.vida.swagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {

	@RequestMapping("/")
	public String swaggerUI(){
		return "redirect:/swagger-ui.html";
	}
}
