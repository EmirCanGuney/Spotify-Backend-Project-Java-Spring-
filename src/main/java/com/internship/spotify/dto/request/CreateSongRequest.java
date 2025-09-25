package com.internship.spotify.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSongRequest {

	@NotBlank(message = "Şarkı adı boş olamaz")
	@Size(max = 200, message = "Şarkı adı 200 karakteri geçemez")
	private String title;

	@Positive(message = "Süre pozitif bir değer olmalı")
	@NotNull(message = "Süre boş olamaz")
	private Integer duration;

	@NotNull(message = "Albüm ID'si boş olamaz")
	private Long albumId;
}
