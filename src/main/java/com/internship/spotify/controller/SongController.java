package com.internship.spotify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.internship.spotify.dto.request.CreateSongRequest;
import com.internship.spotify.dto.request.UpdateSongRequest;
import com.internship.spotify.dto.response.SongResponse;
import com.internship.spotify.service.SongService;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/songs")
public class SongController {

	private final SongService songService;

	@GetMapping
	public ResponseEntity<List<SongResponse>> getAllSongs() {
		List<SongResponse> songs = songService.getAllSong();
		return ResponseEntity.ok(songs);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SongResponse> getSongById(@PathVariable Long id) {

		try {
			SongResponse song = songService.getSongById(id);
			return ResponseEntity.ok(song);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/album/{albumId}")
	public ResponseEntity<List<SongResponse>> getSongsByAlbum(@PathVariable Long albumId) {

		List<SongResponse> songs = songService.getSongsByAlbumId(albumId);
		return ResponseEntity.ok(songs);

	}

	@GetMapping("/search")
	public ResponseEntity<List<SongResponse>> getSongsByTitle(@RequestParam String title) {

		List<SongResponse> songsList = songService.searchSongsByTitle(title);
		return ResponseEntity.ok(songsList);
	}

	@PostMapping
	public ResponseEntity<SongResponse> createSong(@Valid @RequestBody CreateSongRequest request) {

		try {
			SongResponse createdSong = songService.createSong(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdSong);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<SongResponse> updateSong(@PathVariable Long idLong,
			@Valid @RequestBody UpdateSongRequest request) {

		try {
			SongResponse updatedSong = songService.updateSong(idLong, request);
			return ResponseEntity.ok(updatedSong);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSong(@PathVariable Long id) {
		try {
			songService.deleteSong(id);
			return ResponseEntity.ok("Şarkı başarıyla silindi.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Şarkı bulunamadı.");
		}
	}

}
