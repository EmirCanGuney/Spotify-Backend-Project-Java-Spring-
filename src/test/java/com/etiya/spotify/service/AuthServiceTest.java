package com.etiya.spotify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.internship.spotify.dto.request.LoginRequest;
import com.internship.spotify.dto.request.RegisterRequest;
import com.internship.spotify.dto.response.JwtResponse;
import com.internship.spotify.entity.User;
import com.internship.spotify.repository.UserRepository;
import com.internship.spotify.security.JwtUtil;
import com.internship.spotify.service.AuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private AuthenticationManager authenticationManager;

	@InjectMocks
	private AuthService authService;

	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setId(1L);
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("encodedPassword");
		user.setFullName("Test User");
	}

	@Test
	void testRegister_Success() {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("newuser");
		request.setEmail("new@example.com");
		request.setPassword("password123");
		request.setFullName("New User");

		when(userRepository.existsByUsername("newuser")).thenReturn(false);
		when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(jwtUtil.generateToken("testuser")).thenReturn("jwt-token");

		JwtResponse response = authService.register(request);

		assertNotNull(response);
		assertEquals("jwt-token", response.getToken());
		assertEquals("Bearer", response.getType());
		assertEquals("testuser", response.getUsername());
		verify(userRepository).save(any(User.class));
	}

	@Test
	void testRegister_UsernameExists() {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("testuser");
		request.setEmail("new@example.com");

		when(userRepository.existsByUsername("testuser")).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));

		assertTrue(ex.getMessage().contains("Bu kullanıcı adı zaten kullanılıyor"));
	}

	@Test
	void testRegister_EmailExists() {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("newuser");
		request.setEmail("test@example.com");

		when(userRepository.existsByUsername("newuser")).thenReturn(false);
		when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));

		assertTrue(ex.getMessage().contains("Bu email zaten kullanılıyor"));
	}

	@Test
	void testLogin_Success() {
		LoginRequest request = new LoginRequest();
		request.setUsername("testuser");
		request.setPassword("password123");

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(jwtUtil.generateToken("testuser")).thenReturn("jwt-token");

		// authenticationManager.authenticate sadece çağrıldığı kontrol edilir
		doNothing().when(authenticationManager)
				.authenticate(new UsernamePasswordAuthenticationToken("testuser", "password123"));

		JwtResponse response = authService.login(request);

		assertNotNull(response);
		assertEquals("jwt-token", response.getToken());
		assertEquals("testuser", response.getUsername());
		verify(userRepository).findByUsername("testuser");
	}

	@Test
	void testLogin_UserNotFound() {
		LoginRequest request = new LoginRequest();
		request.setUsername("unknown");
		request.setPassword("password123");

		when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> authService.login(request));
	}
}
