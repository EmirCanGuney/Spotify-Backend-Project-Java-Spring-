package com.internship.spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.spotify.entity.Album;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

	List<Album> findByArtistId(Long artistId);

	// buyuk kucuk harf duyarsız ve içeriyorsa getirir
	List<Album> findByTitleContainingIgnoreCase(String title);

	List<Album> findByReleaseYear(Integer releaseYear);

	List<Album> findByArtistNameContainingIgnoreCase(String artistName);
}
