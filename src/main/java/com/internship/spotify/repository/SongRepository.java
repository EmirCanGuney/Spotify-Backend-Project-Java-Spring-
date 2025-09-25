package com.internship.spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.spotify.entity.Song;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

	List<Song> findByAlbumId(Long albumId);

	List<Song> findByTitleContainingIgnoreCase(String title);
}