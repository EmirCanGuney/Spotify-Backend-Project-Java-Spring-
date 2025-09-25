package com.internship.spotify.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PlaylistResponse {
	private Long id;
	private String name;
	private String description;
	private UserResponse user;
	private List<SongResponse> songs;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}