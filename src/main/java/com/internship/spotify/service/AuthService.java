package com.internship.spotify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.internship.spotify.dto.request.LoginRequest;
import com.internship.spotify.dto.request.RegisterRequest;
import com.internship.spotify.dto.response.JwtResponse;
import com.internship.spotify.entity.User;
import com.internship.spotify.repository.UserRepository;
import com.internship.spotify.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtResponse register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Bu email zaten kullanılıyor");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword())); // Şifreyi hashle
		user.setFullName(request.getFullName());

		User savedUser = userRepository.save(user);

		String token = jwtUtil.generateToken(savedUser.getUsername());

		return new JwtResponse(token, "Bearer", 86400L, // 24 saat (saniye)
				savedUser.getUsername(), savedUser.getEmail(), savedUser.getFullName());
	}

	public JwtResponse login(LoginRequest request) {
		// Username ve password doğru mu kontrolü
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

		String token = jwtUtil.generateToken(user.getUsername());

		return new JwtResponse(token, "Bearer", 86400L, user.getUsername(), user.getEmail(), user.getFullName());
	}
}