package com.etiya.spotify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.internship.spotify.dto.request.CreateAlbumRequest;
import com.internship.spotify.dto.request.UpdateAlbumRequest;
import com.internship.spotify.dto.response.AlbumResponse;
import com.internship.spotify.entity.Album;
import com.internship.spotify.entity.Artist;
import com.internship.spotify.repository.AlbumRepository;
import com.internship.spotify.repository.ArtistRepository;
import com.internship.spotify.service.AlbumService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlbumServiceTest {

	@Mock
	private AlbumRepository albumRepository;

	@Mock
	private ArtistRepository artistRepository;

	@InjectMocks
	private AlbumService albumService;

	private Artist artist;
	private Album album;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		artist = new Artist();
		artist.setId(1L);
		artist.setName("Test Artist");
		artist.setImageUrl("artist.jpg");

		album = new Album();
		album.setId(1L);
		album.setTitle("Test Album");
		album.setReleaseYear(2024);
		album.setCoverImageUrl("cover.jpg");
		album.setArtist(artist);
	}

	@Test
	void testGetAllAlbums() {
		when(albumRepository.findAll()).thenReturn(Arrays.asList(album));

		List<AlbumResponse> responses = albumService.getAllAlbums();

		assertEquals(1, responses.size());
		assertEquals("Test Album", responses.get(0).getTitle());
		verify(albumRepository, times(1)).findAll();
	}

	@Test
	void testGetAlbumById_Found() {
		when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

		AlbumResponse response = albumService.getAlbumById(1L);

		assertNotNull(response);
		assertEquals("Test Album", response.getTitle());
		verify(albumRepository).findById(1L);
	}

	@Test
	void testGetAlbumById_NotFound() {
		when(albumRepository.findById(2L)).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class, () -> albumService.getAlbumById(2L));
		assertTrue(ex.getMessage().contains("Albüm bulunamadı"));
	}

	@Test
	void testCreateAlbum() {
		CreateAlbumRequest request = new CreateAlbumRequest();
		request.setTitle("New Album");
		request.setReleaseYear(2025);
		request.setCoverImageUrl("new.jpg");
		request.setArtistId(1L);

		when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
		when(albumRepository.save(any(Album.class))).thenAnswer(i -> {
			Album saved = i.getArgument(0);
			saved.setId(2L);
			return saved;
		});

		AlbumResponse response = albumService.createAlbum(request);

		assertNotNull(response);
		assertEquals("New Album", response.getTitle());
		assertEquals(2L, response.getId());
		verify(albumRepository).save(any(Album.class));
	}

	@Test
	void testUpdateAlbum() {
		UpdateAlbumRequest request = new UpdateAlbumRequest();
		request.setTitle("Updated Title");

		when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
		when(albumRepository.save(any(Album.class))).thenReturn(album);

		AlbumResponse response = albumService.updateAlbum(1L, request);

		assertEquals("Updated Title", response.getTitle());
		verify(albumRepository).save(album);
	}

	@Test
	void testDeleteAlbum_Found() {
		when(albumRepository.existsById(1L)).thenReturn(true);

		albumService.deleteAlbum(1L);

		verify(albumRepository).deleteById(1L);
	}

	@Test
	void testDeleteAlbum_NotFound() {
		when(albumRepository.existsById(2L)).thenReturn(false);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> albumService.deleteAlbum(2L));
		assertTrue(ex.getMessage().contains("Albüm bulunamadı"));
	}
}
