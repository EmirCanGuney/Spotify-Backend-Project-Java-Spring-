package com.etiya.spotify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.internship.spotify.dto.request.CreateUserRequest;
import com.internship.spotify.dto.request.UpdateUserRequest;
import com.internship.spotify.dto.response.UserResponse;
import com.internship.spotify.entity.User;
import com.internship.spotify.exception.BaseException;
import com.internship.spotify.repository.UserRepository;
import com.internship.spotify.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// ---------- getAllUsers ----------
	@Test
	void testGetAllUsers() {
		User user1 = new User();
		user1.setId(1L);
		user1.setUsername("emir");
		user1.setEmail("emir@test.com");

		User user2 = new User();
		user2.setId(2L);
		user2.setUsername("ali");
		user2.setEmail("ali@test.com");

		when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

		List<UserResponse> responses = userService.getAllUsers();

		assertEquals(2, responses.size());
		assertEquals("emir", responses.get(0).getUsername());
		assertEquals("ali", responses.get(1).getUsername());
	}

	// ---------- getUserById ----------
	@Test
	void testGetUserById_UserExists() {
		User user = new User();
		user.setId(1L);
		user.setUsername("emir");
		user.setEmail("emir@test.com");

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		Optional<UserResponse> response = userService.getUserById(1L);

		assertTrue(response.isPresent());
		assertEquals("emir", response.get().getUsername());
	}

	@Test
	void testGetUserById_UserNotExists() {
		when(userRepository.findById(99L)).thenReturn(Optional.empty());

		Optional<UserResponse> response = userService.getUserById(99L);

		assertTrue(response.isEmpty());
	}

	// ---------- getUserByUsername ----------
	@Test
	void testGetUserByUsername() {
		User user = new User();
		user.setId(1L);
		user.setUsername("emir");

		when(userRepository.findByUsername("emir")).thenReturn(Optional.of(user));

		Optional<User> result = userService.getUserByUsername("emir");

		assertTrue(result.isPresent());
		assertEquals("emir", result.get().getUsername());
	}

	// ---------- getUserByEmail ----------
	@Test
	void testGetUserByEmail() {
		User user = new User();
		user.setId(1L);
		user.setEmail("emir@test.com");

		when(userRepository.findByEmail("emir@test.com")).thenReturn(Optional.of(user));

		Optional<User> result = userService.getUserByEmail("emir@test.com");

		assertTrue(result.isPresent());
		assertEquals("emir@test.com", result.get().getEmail());
	}

	// ---------- createUser ----------
	@Test
	void testCreateUser_Success() {
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("emir");
		request.setEmail("emir@test.com");
		request.setPassword("1234");
		request.setFullName("Emir Can");

		User savedUser = new User();
		savedUser.setId(1L);
		savedUser.setUsername("emir");
		savedUser.setEmail("emir@test.com");
		savedUser.setPassword("encodedPass");
		savedUser.setFullName("Emir Can");

		when(userRepository.existsByUsername("emir")).thenReturn(false);
		when(userRepository.existsByEmail("emir@test.com")).thenReturn(false);
		when(passwordEncoder.encode("1234")).thenReturn("encodedPass");
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		UserResponse response = userService.createUser(request);

		assertNotNull(response);
		assertEquals("emir", response.getUsername());
		assertEquals("emir@test.com", response.getEmail());
	}

	@Test
	void testCreateUser_DuplicateUsername() {
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("emir");
		request.setEmail("emir@test.com");
		request.setPassword("1234");

		when(userRepository.existsByUsername("emir")).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(request));

		assertTrue(ex.getMessage().contains("Bu kullanıcı adı zaten kullanılıyor"));
	}

	@Test
	void testCreateUser_DuplicateEmail() {
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("emir");
		request.setEmail("emir@test.com");
		request.setPassword("1234");

		when(userRepository.existsByUsername("emir")).thenReturn(false);
		when(userRepository.existsByEmail("emir@test.com")).thenReturn(true);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(request));

		assertTrue(ex.getMessage().contains("Bu email zaten kullanılıyor"));
	}

	// ---------- updateUser ----------
	@Test
	void testUpdateUser_Success() {
		UpdateUserRequest request = new UpdateUserRequest();
		request.setUsername("ali");
		request.setEmail("ali@test.com");
		request.setFullName("Ali Veli");

		User existingUser = new User();
		existingUser.setId(1L);
		existingUser.setUsername("emir");
		existingUser.setEmail("emir@test.com");
		existingUser.setFullName("Emir Can");

		when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		UserResponse response = userService.updateUser(1L, request);

		assertEquals("ali", response.getUsername());
		assertEquals("ali@test.com", response.getEmail());
		assertEquals("Ali Veli", response.getFullName());
	}

	@Test
	void testUpdateUser_UserNotFound() {
		UpdateUserRequest request = new UpdateUserRequest();
		request.setUsername("ali");
		request.setEmail("ali@test.com");
		request.setFullName("Ali Veli");

		when(userRepository.findById(99L)).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(99L, request));

		assertTrue(ex.getMessage().contains("Kullanıcı bulunamadı"));
	}

	// ---------- deleteUser ----------
	@Test
	void testDeleteUser_Success() {
		when(userRepository.existsById(1L)).thenReturn(true);

		userService.deleteUser(1L);

		verify(userRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeleteUser_UserNotFound() {
		when(userRepository.existsById(99L)).thenReturn(false);

		assertThrows(BaseException.class, () -> userService.deleteUser(99L));
	}
}
