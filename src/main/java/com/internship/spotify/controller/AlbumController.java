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

import com.internship.spotify.dto.request.CreateAlbumRequest;
import com.internship.spotify.dto.request.UpdateAlbumRequest;
import com.internship.spotify.dto.response.AlbumResponse;
import com.internship.spotify.service.AlbumService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/albums")
public class AlbumController {

	private final AlbumService albumService;

	@GetMapping
	public ResponseEntity<List<AlbumResponse>> getAllAlbums() {
		List<AlbumResponse> albums = albumService.getAllAlbums();
		return ResponseEntity.ok(albums);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumResponse> getAlbumById(@PathVariable Long id) {
		try {
			AlbumResponse album = albumService.getAlbumById(id);
			return ResponseEntity.ok(album);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/artist/artistId")
	public ResponseEntity<List<AlbumResponse>> getAlbumsByArtist(@PathVariable Long artistId) {

		List<AlbumResponse> albums = albumService.getAlbumsByArtistId(artistId);
		return ResponseEntity.ok(albums);
	}

	@GetMapping("/search")
	public ResponseEntity<List<AlbumResponse>> getAlbumsByTitle(@RequestParam String title) {

		List<AlbumResponse> albums = albumService.searchAlbumByAlbumTitle(title);
		return ResponseEntity.ok(albums);
	}

	@PostMapping
	public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody CreateAlbumRequest request) {

		try {
			AlbumResponse createdAlbum = albumService.createAlbum(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<AlbumResponse> updateAlbum(@PathVariable Long id,
			@Valid @RequestBody UpdateAlbumRequest request) {

		try {
			AlbumResponse updatedAlbum = albumService.updateAlbum(id, request);
			return ResponseEntity.ok(updatedAlbum);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAlbum(@PathVariable Long id) {
		try {
			albumService.deleteAlbum(id);
			return ResponseEntity.ok("Albüm başarıyla silindi.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Albüm bulunamadı.");
		}
	}

}
