package com.etiya.spotify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.internship.spotify.dto.request.CreateSongRequest;
import com.internship.spotify.dto.request.UpdateSongRequest;
import com.internship.spotify.entity.Album;
import com.internship.spotify.entity.Artist;
import com.internship.spotify.entity.Song;
import com.internship.spotify.repository.AlbumRepository;
import com.internship.spotify.repository.SongRepository;
import com.internship.spotify.service.SongService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SongServiceTest {

	@Mock
	private SongRepository songRepository;

	@Mock
	private AlbumRepository albumRepository;

	@InjectMocks
	private SongService songService;

	private Song song;
	private Album album;
	private Artist artist;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		artist = new Artist();
		artist.setId(1L);
		artist.setName("Artist Name");

		album = new Album();
		album.setId(1L);
		album.setTitle("Album Title");
		album.setArtist(artist);

		song = new Song();
		song.setId(1L);
		song.setTitle("Song Title");
		song.setAlbum(album);
	}

	@Test
	void testGetSongById_Success() {
		when(songRepository.findById(1L)).thenReturn(Optional.of(song));

		var response = songService.getSongById(1L);

		assertNotNull(response);
		assertEquals("Song Title", response.getTitle());
	}

	@Test
	void testGetSongById_NotFound() {
		when(songRepository.findById(2L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> songService.getSongById(2L));
	}

	@Test
	void testCreateSong_Success() {
		CreateSongRequest request = new CreateSongRequest();
		request.setTitle("New Song");
		request.setDuration(300);
		request.setAlbumId(1L);

		when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
		when(songRepository.save(any(Song.class))).thenReturn(song);

		var response = songService.createSong(request);

		assertNotNull(response);
		assertEquals("Song Title", response.getTitle());
		verify(songRepository).save(any(Song.class));
	}

	@Test
	void testUpdateSong_Success() {
		UpdateSongRequest request = new UpdateSongRequest();
		request.setTitle("Updated Song");
		request.setDuration(350);

		when(songRepository.findById(1L)).thenReturn(Optional.of(song));
		when(songRepository.save(any(Song.class))).thenAnswer(i -> i.getArgument(0));

		var response = songService.updateSong(1L, request);

		assertNotNull(response);
		assertEquals("Updated Song", response.getTitle());
		assertEquals(350, response.getDuration());
	}

	@Test
	void testDeleteSong_Success() {
		when(songRepository.existsById(1L)).thenReturn(true);
		doNothing().when(songRepository).deleteById(1L);

		assertDoesNotThrow(() -> songService.deleteSong(1L));
		verify(songRepository).deleteById(1L);
	}

	@Test
	void testDeleteSong_NotFound() {
		when(songRepository.existsById(2L)).thenReturn(false);

		assertThrows(RuntimeException.class, () -> songService.deleteSong(2L));
	}

	@Test
	void testGetAllSong() {
		when(songRepository.findAll()).thenReturn(List.of(song));

		var list = songService.getAllSong();

		assertEquals(1, list.size());
	}
}
