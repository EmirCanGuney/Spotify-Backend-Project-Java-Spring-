package com.internship.spotify.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AlbumResponse {

	private Long id;
	private String title;
	private Integer releaseYear;
	private String coverImageUrl;
	private ArtistSummaryResponse artist;
	private List<SongSummaryResponse> songs;
	private LocalDateTime createdAt;
}