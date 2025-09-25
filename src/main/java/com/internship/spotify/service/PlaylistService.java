package com.internship.spotify.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.internship.spotify.dto.request.AddSongsToPlaylistRequest;
import com.internship.spotify.dto.request.CreatePlaylistRequest;
import com.internship.spotify.dto.request.UpdatePlaylistRequest;
import com.internship.spotify.dto.response.PlaylistResponse;
import com.internship.spotify.entity.Playlist;
import com.internship.spotify.entity.Song;
import com.internship.spotify.entity.User;
import com.internship.spotify.repository.PlaylistRepository;
import com.internship.spotify.repository.SongRepository;
import com.internship.spotify.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistService {

	private final PlaylistRepository playlistRepository;
	private final UserRepository userRepository;
	private final SongRepository songRepository;

	private PlaylistResponse convertToResponse(Playlist playlist) {
		PlaylistResponse response = new PlaylistResponse();
		response.setId(playlist.getId());
		response.setCreatedAt(playlist.getCreatedAt());
		response.setName(playlist.getName());
		// response.setSongs(playlist.getSongs());
		response.setDescription(playlist.getDescription());
		response.setUpdatedAt(playlist.getUpdatedAt());

		return response;
	}

	public List<PlaylistResponse> getAllPlaylists() {
		return playlistRepository.findAll().stream().map(this::convertToResponse).toList();
	}

	public PlaylistResponse getPlaylistById(Long id) {
		Playlist playlist = playlistRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Playlist bulunamadı: " + id));
		return convertToResponse(playlist);
	}

	public List<PlaylistResponse> getPlaylistByUserId(Long UserId) {
		return playlistRepository.findByUserId(UserId).stream().map(this::convertToResponse).toList();
	}

	public List<PlaylistResponse> getPlaylistByTitle(String title) {
		return playlistRepository.findByNameContainingIgnoreCase(title).stream().map(this::convertToResponse).toList();
	}

	public PlaylistResponse createPlaylist(CreatePlaylistRequest request, Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

		Playlist playlist = new Playlist();
		playlist.setName(request.getName());
		playlist.setDescription(request.getDescription());
		playlist.setUser(user);

		// best practices db den her zaman güncel halini almak için
		Playlist savedPlaylist = playlistRepository.save(playlist);

		return convertToResponse(savedPlaylist);
	}

	public PlaylistResponse updatePlaylist(Long id, UpdatePlaylistRequest request, Long userId) {
		validatePlaylistOwnership(userId, id);

		Playlist playlist = playlistRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Playlist bulunamadı: " + id));

		if (request.getName() != null) {
			playlist.setName(request.getName());
		}
		if (request.getDescription() != null) {
			playlist.setDescription(request.getDescription());
		}

		Playlist updatedPlaylist = playlistRepository.save(playlist);
		return convertToResponse(updatedPlaylist);
	}

	// bu kod üzerinde calıs
	public PlaylistResponse addSongsToPlayList(Long playlistId, AddSongsToPlaylistRequest request) {

		Playlist playlist = playlistRepository.findById(playlistId)
				.orElseThrow(() -> new RuntimeException("Playlist bulunamadı: " + playlistId));

		List<Song> songsToAddList = songRepository.findAllById(request.getSongIds());

		for (Song song : songsToAddList) {
			if (!playlist.getSongs().contains(song)) {
				playlist.getSongs().add(song);
			}
		}
		Playlist updatedPlaylist = playlistRepository.save(playlist);
		return convertToResponse(updatedPlaylist);

	}

	public void deleteSongFromPlaylist(Long playlistId, Long songId) {
		Playlist playlist = playlistRepository.findById(playlistId)
				.orElseThrow(() -> new RuntimeException("Playlist bulunamadı: " + playlistId));

		Song song = songRepository.findById(songId)
				.orElseThrow(() -> new RuntimeException("Şarkı bulunamadı: " + songId));

		playlist.getSongs().remove(song);
		playlistRepository.save(playlist);
	}

	public void deletePlaylist(Long id, Long userId) {
		validatePlaylistOwnership(userId, id);

		if (!playlistRepository.existsById(id)) {
			throw new RuntimeException("Playlist bulunamadı: " + id);
		}
		playlistRepository.deleteById(id);
	}

	// Bu metodları PlaylistService sınıfının sonuna ekle
	private boolean canUserModifyPlaylist(Long userId, Long playlistId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

		// Admin ise tüm playlists'leri düzenleyebilir
		if (user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()))) {
			return true;
		}

		// Normal kullanıcı sadece kendi playlist'lerini düzenleyebilir
		Playlist playlist = playlistRepository.findById(playlistId)
				.orElseThrow(() -> new RuntimeException("Playlist bulunamadı"));

		return playlist.getUser().getId().equals(userId);
	}

	private void validatePlaylistOwnership(Long userId, Long playlistId) {
		if (!canUserModifyPlaylist(userId, playlistId)) {
			throw new RuntimeException("Bu playlist'i düzenleme yetkiniz yok");
		}
	}
}
