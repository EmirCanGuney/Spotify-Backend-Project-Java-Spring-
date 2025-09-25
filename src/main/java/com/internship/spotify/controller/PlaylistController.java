package com.internship.spotify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internship.spotify.dto.request.AddSongsToPlaylistRequest;
import com.internship.spotify.dto.request.CreatePlaylistRequest;
import com.internship.spotify.dto.request.UpdatePlaylistRequest;
import com.internship.spotify.dto.response.PlaylistResponse;
import com.internship.spotify.entity.User;
import com.internship.spotify.service.PlaylistService;
import com.internship.spotify.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
public class PlaylistController {

	private final PlaylistService playlistService;

	private final UserService userService;

	@GetMapping
	public ResponseEntity<List<PlaylistResponse>> getAllPlaylists() {
		List<PlaylistResponse> playlists = playlistService.getAllPlaylists();
		return ResponseEntity.ok(playlists);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlaylistResponse> getPlaylistById(@PathVariable Long id) {

		try {
			PlaylistResponse playlist = playlistService.getPlaylistById(id);
			return ResponseEntity.ok(playlist);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<PlaylistResponse>> getPlaylistByUserId(@PathVariable Long userId) {

		try {
			List<PlaylistResponse> playlists = playlistService.getPlaylistByUserId(userId);
			return ResponseEntity.ok(playlists);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{userId}")
	public ResponseEntity<PlaylistResponse> createPlaylist(@Valid @RequestBody CreatePlaylistRequest request,
			@PathVariable Long userId) {
		try {
			PlaylistResponse createdPlaylist = playlistService.createPlaylist(request, userId);
			return ResponseEntity.ok(createdPlaylist);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<PlaylistResponse> updatePlaylist(@PathVariable Long id,
			@Valid @RequestBody UpdatePlaylistRequest request) {

		try {
			Long currentUserId = getCurrentUserId();
			PlaylistResponse updatedPlaylist = playlistService.updatePlaylist(id, request, currentUserId);
			return ResponseEntity.ok(updatedPlaylist);
		} catch (RuntimeException e) {
			if (e.getMessage().contains("yetkiniz yok")) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{playlistId}/songs")
	public ResponseEntity<PlaylistResponse> addSongsToPlaylist(@PathVariable Long playlistId,
			@RequestBody AddSongsToPlaylistRequest request) {

		try {
			PlaylistResponse updatedPlaylist = playlistService.addSongsToPlayList(playlistId, request);
			return ResponseEntity.ok(updatedPlaylist);

		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{playlistId}/songs/{songId}")
	public ResponseEntity<String> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
		try {
			playlistService.deleteSongFromPlaylist(playlistId, songId);
			return ResponseEntity.ok("Şarkı playlist'ten çıkarıldı.");
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// .notFound() sadece build() ile boş döner, body eklemek için
	// status(HttpStatus.NOT_FOUND).body(...) kullanılır.

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePlaylist(@PathVariable Long id) {
		try {
			Long currentUserId = getCurrentUserId();
			playlistService.deletePlaylist(id, currentUserId);
			return ResponseEntity.ok("Playlist başarıyla silindi");
		} catch (RuntimeException e) {
			if (e.getMessage().contains("yetkiniz yok")) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu playlist'i silme yetkiniz yok");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Playlist bulunamadı.");
		}
	}

	// HttpStatus = hazır sabitler, HttpStatusCode = daha esnek, interface tabanlı.

	// Çok veri → Body kullan amaaa Tek ID → Path kullan

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		return userService.getUserByUsername(username).map(User::getId)
				.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + username));
	}
}
