package com.internship.spotify.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

	@Size(max = 50, message = "Kullanıcı adı 50 karakteri geçemez")
	private String username;

	@Email(message = "Geçerli bir email adresi girin")
	private String email;

	@Size(max = 100, message = "Ad soyad 100 karakteri geçemez")
	private String fullName;

	@Size(min = 6, message = "Şifre en az 6 karakter olmalı")
	private String password;
}
