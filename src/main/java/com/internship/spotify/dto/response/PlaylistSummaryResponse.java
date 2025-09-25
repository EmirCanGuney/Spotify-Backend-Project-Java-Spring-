package com.internship.spotify.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlaylistSummaryResponse {

	private Long id;
	private String name;
	private String description;
	private Integer songCount;
	private LocalDateTime createdAt;
}