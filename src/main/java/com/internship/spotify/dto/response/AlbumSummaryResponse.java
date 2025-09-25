package com.internship.spotify.dto.response;

import lombok.Data;

@Data
public class AlbumSummaryResponse {

	private Long id;
	private String title;
	private Integer releaseYear;
	private String coverImageUrl;
}