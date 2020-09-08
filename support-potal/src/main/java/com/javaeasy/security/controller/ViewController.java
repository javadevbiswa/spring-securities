package com.javaeasy.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewController {

	@GetMapping(value = "/home")
	public String showHomePage() {
		return "index";
	}

	@GetMapping(value = "/error-path")
	public String showErrorPage() {
		return "error-path";
	}

}
