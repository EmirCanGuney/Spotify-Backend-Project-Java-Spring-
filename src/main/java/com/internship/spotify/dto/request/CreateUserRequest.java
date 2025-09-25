package com.internship.spotify.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

	@NotBlank(message = "Kullanıcı adı boş olamaz")
	@Size(max = 50, message = "Kullanıcı adı 50 karakteri geçemez")
	private String username;

	@Email(message = "Geçerli bir email adresi girin")
	@NotBlank(message = "Email boş olamaz")
	private String email;

	@NotBlank(message = "Şifre boş olamaz")
	@Size(min = 6, message = "Şifre en az 6 karakter olmalı")
	private String password;

	@Size(max = 100, message = "Ad soyad 100 karakteri geçemez")
	private String fullName;

}