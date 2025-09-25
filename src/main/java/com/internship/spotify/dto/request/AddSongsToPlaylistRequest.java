package com.internship.spotify.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class AddSongsToPlaylistRequest {

	private List<Long> songIds;
}