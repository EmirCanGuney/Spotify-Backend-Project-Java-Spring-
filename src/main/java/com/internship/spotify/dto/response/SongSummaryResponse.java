package com.internship.spotify.dto.response;

import lombok.Data;

@Data
public class SongSummaryResponse {

	private Long id;
	private String title;
	private Integer duration;
}