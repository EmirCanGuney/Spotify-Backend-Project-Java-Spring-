package com.internship.spotify.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserResponse {

	private Long id;
	private String username;
	private String email;
	private String fullName;
	private LocalDateTime createdAt;
	private List<PlaylistSummaryResponse> playlists;
}
