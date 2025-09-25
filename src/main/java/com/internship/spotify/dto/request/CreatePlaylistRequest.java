package com.internship.spotify.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePlaylistRequest {

	@NotBlank(message = "Playlist adı boş olamaz")
	@Size(max = 100, message = "Playlist adı 100 karakteri geçemez")
	private String name;

	@Size(max = 500, message = "Açıklama 500 karakteri geçemez")
	private String description;
}