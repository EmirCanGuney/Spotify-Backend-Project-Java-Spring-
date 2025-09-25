package com.internship.spotify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.internship.spotify.dto.request.CreateArtistRequest;
import com.internship.spotify.dto.request.UpdateArtistRequest;
import com.internship.spotify.dto.response.ArtistResponse;
import com.internship.spotify.service.ArtistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

	private final ArtistService artistService;

	@GetMapping
	public ResponseEntity<List<ArtistResponse>> getAllArtists() {
		List<ArtistResponse> artists = artistService.getAllArtists();
		return ResponseEntity.ok(artists);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ArtistResponse> getArtistById(@PathVariable Long id) {
		try {
			ArtistResponse artist = artistService.getArtistById(id);
			return ResponseEntity.ok(artist);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
			// build yazmazsan body siz compile atmez mecburi boş build ekleriz
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<ArtistResponse>> searchArtists(@RequestParam String name) {

		List<ArtistResponse> searchedArtists = artistService.searchArtistByName(name);
		return ResponseEntity.ok(searchedArtists);
	}

	@PostMapping
	public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody CreateArtistRequest request) {
		// @valid DTO-daki validation kurallarını kontrol ediyor
		try {
			ArtistResponse createdArtist = artistService.createArtist(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ArtistResponse> updateArtist(@PathVariable Long id,
			@Valid @RequestBody UpdateArtistRequest request) {

		try {
			ArtistResponse updatedArtist = artistService.updateArtist(id, request);
			return ResponseEntity.ok(updatedArtist);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteArtist(@PathVariable Long id) {
		try {
			artistService.deleteArtist(id);
			return ResponseEntity.ok("Sanatçı başarıyla silindi.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sanatçı bulunamadı.");
		}
	}

	@GetMapping("/count")
	public ResponseEntity<String> countArtists() {
		String countString = artistService.countArtistsString();
		return ResponseEntity.ok("Uygulamada bulunan toplam artist sayısı: " + countString);
	}

}
