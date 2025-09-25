package com.internship.spotify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/protected")
	public ResponseEntity<?> protectedEndpoint(Authentication auth) {
		return ResponseEntity.ok("Merhaba " + auth.getName() + " Token is working bro");
	}
}