package com.internship.spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.spotify.entity.Playlist;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

	List<Playlist> findByUserId(Long userId);

	List<Playlist> findByNameContainingIgnoreCase(String name);
}