package com.internship.spotify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.internship.spotify.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
