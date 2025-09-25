package com.etiya.spotify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.internship.spotify.dto.request.CreateArtistRequest;
import com.internship.spotify.dto.request.UpdateArtistRequest;
import com.internship.spotify.dto.response.ArtistResponse;
import com.internship.spotify.entity.Artist;
import com.internship.spotify.repository.ArtistRepository;
import com.internship.spotify.service.ArtistService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArtistServiceTest {

	@Mock
	private ArtistRepository artistRepository;

	@InjectMocks
	private ArtistService artistService;

	private Artist artist;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		artist = new Artist();
		artist.setId(1L);
		artist.setName("Test Artist");
		artist.setBio("Test Bio");
		artist.setImageUrl("artist.jpg");
	}

	@Test
	void testGetAllArtists() {
		when(artistRepository.findAll()).thenReturn(Arrays.asList(artist));

		List<ArtistResponse> responses = artistService.getAllArtists();

		assertEquals(1, responses.size());
		assertEquals("Test Artist", responses.get(0).getName());
		verify(artistRepository, times(1)).findAll();
	}

	@Test
	void testGetArtistById_Found() {
		when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

		ArtistResponse response = artistService.getArtistById(1L);

		assertNotNull(response);
		assertEquals("Test Artist", response.getName());
		verify(artistRepository).findById(1L);
	}

	@Test
	void testGetArtistById_NotFound() {
		when(artistRepository.findById(2L)).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class, () -> artistService.getArtistById(2L));
		assertTrue(ex.getMessage().contains("Sanatçı bulunamadı"));
	}

	@Test
	void testSearchArtistByName() {
		when(artistRepository.findByNameContainingIgnoreCase("Test")).thenReturn(Arrays.asList(artist));

		List<ArtistResponse> responses = artistService.searchArtistByName("Test");

		assertEquals(1, responses.size());
		assertEquals("Test Artist", responses.get(0).getName());
		verify(artistRepository).findByNameContainingIgnoreCase("Test");
	}

	@Test
	void testCreateArtist() {
		CreateArtistRequest request = new CreateArtistRequest();
		request.setName("New Artist");
		request.setBio("New Bio");
		request.setImageUrl("new.jpg");

		when(artistRepository.save(any(Artist.class))).thenAnswer(i -> {
			Artist saved = i.getArgument(0);
			saved.setId(2L);
			return saved;
		});

		ArtistResponse response = artistService.createArtist(request);

		assertNotNull(response);
		assertEquals("New Artist", response.getName());
		assertEquals(2L, response.getId());
		verify(artistRepository).save(any(Artist.class));
	}

	@Test
	void testUpdateArtist() {
		UpdateArtistRequest request = new UpdateArtistRequest();
		request.setName("Updated Name");

		when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
		when(artistRepository.save(any(Artist.class))).thenReturn(artist);

		ArtistResponse response = artistService.updateArtist(1L, request);

		assertEquals("Updated Name", response.getName());
		verify(artistRepository).save(artist);
	}

	@Test
	void testDeleteArtist_Found() {
		when(artistRepository.existsById(1L)).thenReturn(true);

		artistService.deleteArtist(1L);

		verify(artistRepository).deleteById(1L);
	}

	@Test
	void testDeleteArtist_NotFound() {
		when(artistRepository.existsById(2L)).thenReturn(false);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> artistService.deleteArtist(2L));
		assertTrue(ex.getMessage().contains("Sanatçı bulunamadı"));
	}

	@Test
	void testCountArtistsString() {
		when(artistRepository.count()).thenReturn(5L);

		String count = artistService.countArtistsString();

		assertEquals("5", count);
		verify(artistRepository).count();
	}
}
