package com.internship.spotify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.internship.spotify.dto.request.CreateUserRequest;
import com.internship.spotify.dto.request.UpdateUserRequest;
import com.internship.spotify.dto.response.UserResponse;
import com.internship.spotify.entity.User;
import com.internship.spotify.exception.BaseException;
import com.internship.spotify.exception.ErrorMessage;
import com.internship.spotify.exception.MessageType;
import com.internship.spotify.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private UserResponse convertToResponse(User user) {

		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setFullName(user.getFullName());
		response.setCreatedAt(user.getCreatedAt());
		return response;
	}

	// RequiredArgsConstructor ve final türü sayesinde UserService her zaman geçerli
	// ve değiştirilemez bir repository ile çalışıyor.
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::convertToResponse).toList();
	}

	public Optional<UserResponse> getUserById(Long id) {
		return userRepository.findById(id).map(this::convertToResponse);

	}

	// dtosuz çünkü iç yapıda kullanılacak methodalar
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public UserResponse createUser(CreateUserRequest createUserRequest) {
		if (userRepository.existsByUsername(createUserRequest.getUsername())) {
			throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor: " + createUserRequest.getUsername());
		}
		if (userRepository.existsByEmail(createUserRequest.getEmail())) {
			throw new RuntimeException("Bu email zaten kullanılıyor: " + createUserRequest.getEmail());
		}

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setEmail(createUserRequest.getEmail());
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		user.setFullName(createUserRequest.getFullName());

		// map methodu save de yok optional ve streamde vardı
		User savedUser = userRepository.save(user);
		return convertToResponse(savedUser);
	}

	public UserResponse updateUser(Long id, UpdateUserRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + id));

		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setFullName(request.getFullName());

		User updatedUser = userRepository.save(user);

		return convertToResponse(updatedUser);
	}

	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString()));
		}

		userRepository.deleteById(id);
	}

}
