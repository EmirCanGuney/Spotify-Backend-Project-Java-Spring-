package com.internship.spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.spotify.entity.Artist;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

	List<Artist> findByNameContainingIgnoreCase(String name);

}
