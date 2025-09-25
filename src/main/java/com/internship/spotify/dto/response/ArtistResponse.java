package com.internship.spotify.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArtistResponse {
	private Long id;
	private String name;
	private String bio;
	private String imageUrl;
	private LocalDateTime createdAt;
	private List<AlbumSummaryResponse> albums;
}