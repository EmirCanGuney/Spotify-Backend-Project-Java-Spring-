package com.internship.spotify.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.internship.spotify.dto.request.CreateAlbumRequest;
import com.internship.spotify.dto.request.UpdateAlbumRequest;
import com.internship.spotify.dto.response.AlbumResponse;
import com.internship.spotify.dto.response.ArtistSummaryResponse;
import com.internship.spotify.entity.Album;
import com.internship.spotify.entity.Artist;
import com.internship.spotify.repository.AlbumRepository;
import com.internship.spotify.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {

	private final AlbumRepository albumRepository;
	private final ArtistRepository artistRepository;

	public AlbumResponse convertToResponse(Album album) {
		AlbumResponse response = new AlbumResponse();
		response.setId(album.getId());
		response.setTitle(album.getTitle());
		response.setReleaseYear(album.getReleaseYear());
		response.setCoverImageUrl(album.getCoverImageUrl());
		response.setCreatedAt(album.getCreatedAt());

		// artist bilgisi
		ArtistSummaryResponse artistSummaryResponse = new ArtistSummaryResponse();
		artistSummaryResponse.setId(album.getArtist().getId());
		artistSummaryResponse.setName(album.getArtist().getName());
		artistSummaryResponse.setImageUrl(album.getArtist().getImageUrl());

		response.setArtist(artistSummaryResponse);
		return response;

	}

	public List<AlbumResponse> getAllAlbums() {
		return albumRepository.findAll().stream().map(this::convertToResponse).toList();
	}

	public AlbumResponse getAlbumById(Long id) {
		Album album = albumRepository.findById(id).orElseThrow(() -> new RuntimeException("Albüm bulunamadı: " + id));
		return convertToResponse(album);
	}

	public List<AlbumResponse> getAlbumsByArtistId(Long artistId) {
		return albumRepository.findByArtistId(artistId).stream().map(this::convertToResponse).toList();
	}

	public List<AlbumResponse> searchAlbumByAlbumTitle(String title) {
		return albumRepository.findByTitleContainingIgnoreCase(title).stream().map(this::convertToResponse).toList();
	}

	public AlbumResponse createAlbum(CreateAlbumRequest request) {
		Artist artist = artistRepository.findById(request.getArtistId())
				.orElseThrow(() -> new RuntimeException("Sanatçı bulunamadı: " + request.getArtistId()));

		Album album = new Album();
		album.setTitle(request.getTitle());
		album.setReleaseYear(request.getReleaseYear());
		album.setCoverImageUrl(request.getCoverImageUrl());
		album.setArtist(artist);

		Album savedAlbum = albumRepository.save(album);
		return convertToResponse(savedAlbum);
	}

	public AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request) {
		Album album = albumRepository.findById(id).orElseThrow(() -> new RuntimeException("Albüm bulunamadı: " + id));

		if (request.getTitle() != null) {
			album.setTitle(request.getTitle());
		}
		if (request.getReleaseYear() != null) {
			album.setReleaseYear(request.getReleaseYear());
		}
		if (request.getCoverImageUrl() != null) {
			album.setCoverImageUrl(request.getCoverImageUrl());
		}

		Album updatedAlbum = albumRepository.save(album);
		return convertToResponse(updatedAlbum);
	}

	public void deleteAlbum(Long id) {
		if (!albumRepository.existsById(id)) {
			throw new RuntimeException("Albüm bulunamadı: " + id);
		}
		albumRepository.deleteById(id);
	}

}
