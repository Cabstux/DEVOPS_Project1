package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringController {

	
	
	@GetMapping(value = "/")
	public String getValue() {


	

		String result = "Hello Word Mr Sene";
		return result;
	}
}
