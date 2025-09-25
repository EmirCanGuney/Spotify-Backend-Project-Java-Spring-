package com.etiya.spotify.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.internship.spotify.dto.request.AddSongsToPlaylistRequest;
import com.internship.spotify.dto.request.CreatePlaylistRequest;
import com.internship.spotify.dto.request.UpdatePlaylistRequest;
import com.internship.spotify.dto.response.PlaylistResponse;
import com.internship.spotify.entity.Playlist;
import com.internship.spotify.entity.Role;
import com.internship.spotify.entity.Song;
import com.internship.spotify.entity.User;
import com.internship.spotify.repository.PlaylistRepository;
import com.internship.spotify.repository.SongRepository;
import com.internship.spotify.repository.UserRepository;
import com.internship.spotify.service.PlaylistService;

class PlaylistServiceTest {

	@Mock
	private PlaylistRepository playlistRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SongRepository songRepository;

	@InjectMocks
	private PlaylistService playlistService;

	private User user;
	private Playlist playlist;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setId(1L);
		user.setUsername("testUser");

		playlist = new Playlist();
		playlist.setId(1L);
		playlist.setName("My Playlist");
		playlist.setDescription("Test Description");
		playlist.setUser(user);
		playlist.setSongs(new ArrayList<>());
	}

	@Test
	void testCreatePlaylist_Success() {
		CreatePlaylistRequest request = new CreatePlaylistRequest();
		request.setName("New Playlist");
		request.setDescription("Desc");

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

		PlaylistResponse response = playlistService.createPlaylist(request, 1L);

		assertNotNull(response);
		assertEquals("My Playlist", response.getName());
		verify(playlistRepository, times(1)).save(any(Playlist.class));
	}

	@Test
	void testUpdatePlaylist_Success() {
		UpdatePlaylistRequest request = new UpdatePlaylistRequest();
		request.setName("Updated");
		request.setDescription("Updated Desc");

		// User is owner
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
		when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

		PlaylistResponse response = playlistService.updatePlaylist(1L, request, 1L);

		assertEquals("My Playlist", response.getName()); // çünkü mock save aynı objeyi döndürüyor
		verify(playlistRepository, times(1)).save(any(Playlist.class));
	}

	@Test
	void testUpdatePlaylist_Unauthorized_ThrowsException() {
		User anotherUser = new User();
		anotherUser.setId(2L);
		anotherUser.setUsername("other");

		when(userRepository.findById(2L)).thenReturn(Optional.of(anotherUser));
		when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> playlistService.updatePlaylist(1L, new UpdatePlaylistRequest(), 2L));

		assertTrue(ex.getMessage().contains("yetkiniz yok"));
	}

	@Test
	void testAddSongsToPlaylist_Success() {
		Song song = new Song();
		song.setId(1L);
		song.setTitle("Test Song");

		when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
		when(songRepository.findAllById(List.of(1L))).thenReturn(List.of(song));
		when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

		AddSongsToPlaylistRequest request = new AddSongsToPlaylistRequest();
		request.setSongIds(List.of(1L));

		PlaylistResponse response = playlistService.addSongsToPlayList(1L, request);

		assertNotNull(response);
		assertTrue(playlist.getSongs().contains(song));
	}

	@Test
	void testDeletePlaylist_Success() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
		when(playlistRepository.existsById(1L)).thenReturn(true);

		playlistService.deletePlaylist(1L, 1L);

		verify(playlistRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeletePlaylist_NotFound_ThrowsException() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
		when(playlistRepository.existsById(1L)).thenReturn(false);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> playlistService.deletePlaylist(1L, 1L));

		assertTrue(ex.getMessage().contains("bulunamadı"));
	}
}
