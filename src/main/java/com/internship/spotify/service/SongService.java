package com.internship.spotify.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.internship.spotify.dto.request.CreateSongRequest;
import com.internship.spotify.dto.request.UpdateSongRequest;
import com.internship.spotify.dto.response.AlbumSummaryResponse;
import com.internship.spotify.dto.response.ArtistSummaryResponse;
import com.internship.spotify.dto.response.SongResponse;
import com.internship.spotify.entity.Album;
import com.internship.spotify.entity.Song;
import com.internship.spotify.repository.AlbumRepository;
import com.internship.spotify.repository.SongRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongService {

	private final SongRepository songRepository;
	private final AlbumRepository albumRepository;

	private SongResponse convertToResponse(Song song) {
		SongResponse response = new SongResponse();
		response.setId(song.getId());
		response.setTitle(song.getTitle());
		response.setDuration(song.getDuration());
		response.setCreatedAt(song.getCreatedAt());

		AlbumSummaryResponse albumSummaryResponse = new AlbumSummaryResponse();
		albumSummaryResponse.setId(song.getAlbum().getId());
		albumSummaryResponse.setTitle(song.getAlbum().getTitle());
		albumSummaryResponse.setReleaseYear(song.getAlbum().getReleaseYear());
		albumSummaryResponse.setCoverImageUrl(song.getAlbum().getCoverImageUrl());

		response.setAlbum(albumSummaryResponse);

		ArtistSummaryResponse artistResponse = new ArtistSummaryResponse();
		artistResponse.setId(song.getAlbum().getArtist().getId());
		artistResponse.setName(song.getAlbum().getArtist().getName());
		artistResponse.setImageUrl(song.getAlbum().getArtist().getImageUrl());

		response.setArtist(artistResponse);

		return response;
	}

	public List<SongResponse> getAllSong() {
		return songRepository.findAll().stream().map(this::convertToResponse).toList();
	}

	public SongResponse getSongById(Long id) {
		Song song = songRepository.findById(id).orElseThrow(() -> new RuntimeException("Şarkı bulunamadı: " + id));
		return convertToResponse(song);
	}

	public List<SongResponse> getSongsByAlbumId(Long albumId) {
		return songRepository.findByAlbumId(albumId).stream().map(this::convertToResponse).toList();
	}

	public List<SongResponse> searchSongsByTitle(String title) {

		return songRepository.findByTitleContainingIgnoreCase(title).stream().map(this::convertToResponse).toList();
	}

	public SongResponse createSong(CreateSongRequest request) {

		Album album = albumRepository.findById(request.getAlbumId())
				.orElseThrow(() -> new RuntimeException("Albüm bulunamadı: " + request.getAlbumId()));

		Song song = new Song();
		song.setTitle(request.getTitle());
		song.setDuration(request.getDuration());
		song.setAlbum(album);

		Song savedSong = songRepository.save(song);
		return convertToResponse(savedSong);
	}

	public SongResponse updateSong(Long id, UpdateSongRequest request) {
		Song song = songRepository.findById(id).orElseThrow(() -> new RuntimeException("Şarkı Bulunamadı: " + id));

		if (request.getTitle() != null) {
			song.setTitle(request.getTitle());
		}
		if (request.getDuration() != null) {
			song.setDuration(request.getDuration());
		}

		Song updatedSong = songRepository.save(song);

		return convertToResponse(updatedSong);

	}

	public void deleteSong(Long id) {
		if (!songRepository.existsById(id)) {
			throw new RuntimeException("Şarkı bulunamadı: " + id);
		}
		songRepository.deleteById(id);
	}

}
