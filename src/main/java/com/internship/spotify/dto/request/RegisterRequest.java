package com.internship.spotify.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

	@NotBlank(message = "Kullanıcı adı boş olamaz")
	@Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arasında olmalı")
	private String username;

	@NotBlank(message = "Email boş olamaz")
	@Email(message = "Geçerli bir email adresi girin")
	@Size(max = 100, message = "Email 100 karakteri geçemez")
	private String email;

	@NotBlank(message = "Şifre boş olamaz")
	@Size(min = 6, max = 20, message = "Şifre 6-20 karakter arasında olmalı")
	private String password;

	@Size(max = 100, message = "Ad soyad 100 karakteri geçemez")
	private String fullName; // Opsiyonel
}
