package com.internship.spotify.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSongRequest {

	@Size(max = 200, message = "Şarkı adı 200 karakteri geçemez")
	private String title;

	@Positive(message = "Süre pozitif bir değer olmalı")
	private Integer duration;

}