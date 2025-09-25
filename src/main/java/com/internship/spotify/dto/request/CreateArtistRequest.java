package com.internship.spotify.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateArtistRequest {

	@NotBlank(message = "Sanatçı adı boş olamaz")
	@Size(max = 100, message = "Sanatçı adı 100 karakteri geçemez")
	private String name;

	@Size(max = 1000, message = "Biyografi 1000 karakteri geçemez")
	private String bio;

	@Size(max = 500, message = "Resim URL'i 500 karakteri geçemez")
	private String imageUrl;
}
