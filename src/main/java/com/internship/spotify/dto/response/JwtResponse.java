package com.internship.spotify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

	private String token;
	private String type = "Bearer"; // Token tipi
	private Long expiresIn; // Kaç saniye sonra expire olacak
	private String username;
	private String email;
	private String fullName;
}