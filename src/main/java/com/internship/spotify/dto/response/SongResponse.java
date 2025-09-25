package com.internship.spotify.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SongResponse {

	private Long id;
	private String title;
	private Integer duration;
	private AlbumSummaryResponse album;
	private ArtistSummaryResponse artist;
	private LocalDateTime createdAt;
}