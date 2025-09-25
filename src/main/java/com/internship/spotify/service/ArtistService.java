package com.internship.spotify.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.internship.spotify.dto.request.CreateArtistRequest;
import com.internship.spotify.dto.request.UpdateArtistRequest;
import com.internship.spotify.dto.response.ArtistResponse;
import com.internship.spotify.entity.Artist;
import com.internship.spotify.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistService {

	private final ArtistRepository artistRepository;

	private ArtistResponse convertToResponse(Artist artist) {
		ArtistResponse response = new ArtistResponse();
		response.setId(artist.getId());
		response.setName(artist.getName());
		response.setBio(artist.getBio());
		response.setImageUrl(artist.getImageUrl());
		response.setCreatedAt(artist.getCreatedAt());
		return response;
	}

	public List<ArtistResponse> getAllArtists() {
		return artistRepository.findAll().stream().map(this::convertToResponse).toList();
	}

	public ArtistResponse getArtistById(Long id) {
		Artist artist = artistRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Sanatçı bulunamadı: " + id));
		return convertToResponse(artist);
	}

	public List<ArtistResponse> searchArtistByName(String name) {
		return artistRepository.findByNameContainingIgnoreCase(name).stream().map(this::convertToResponse).toList();
	}

	public ArtistResponse createArtist(CreateArtistRequest request) {
		Artist artist = new Artist();
		artist.setName(request.getName());
		artist.setBio(request.getBio());
		artist.setImageUrl(request.getImageUrl());

		Artist savedArtist = artistRepository.save(artist);
		return convertToResponse(savedArtist);

	}

	public ArtistResponse updateArtist(Long id, UpdateArtistRequest request) {
		Artist artist = artistRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Sanatçı bulunamadı: " + id));

		// sadece seçilen veriyi değişmek diğerlerine karışmamak için
		if (request.getName() != null) {
			artist.setName(request.getName());
		}
		if (request.getBio() != null) {
			artist.setBio(request.getBio());
		}
		if (request.getImageUrl() != null) {
			artist.setImageUrl(request.getImageUrl());
		}
		Artist updatedArtist = artistRepository.save(artist);
		return convertToResponse(updatedArtist);

	}

	public void deleteArtist(Long id) {
		if (!artistRepository.existsById(id)) {
			throw new RuntimeException("Sanatçı bulunamadı: " + id);
		}
		artistRepository.deleteById(id);
	}

	public String countArtistsString() {
		Long countLong = artistRepository.count();
		String countStr = countLong.toString();
		return countStr;
	}

}
