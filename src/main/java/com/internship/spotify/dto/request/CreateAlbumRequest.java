package com.internship.spotify.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAlbumRequest {

	@NotBlank(message = "Albüm adı boş olamaz")
	@Size(max = 200, message = "Albüm adı 200 karakteri geçemez")
	private String title;

	@Min(value = 1900, message = "Çıkış yılı 1900'den küçük olamaz")
	private Integer releaseYear;

	@Size(max = 500, message = "Kapak resmi URL'i 500 karakteri geçemez")
	private String coverImageUrl;

	@NotNull(message = "Sanatçı ID'si boş olamaz")
	private Long artistId;
}