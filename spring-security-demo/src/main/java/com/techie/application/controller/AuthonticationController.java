package com.techie.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/auth")
public class AuthonticationController {

	@GetMapping(value = "/getMsg")
	public String getMessage() {
		return "Hello ! This is my First spring security application";
	}
}
