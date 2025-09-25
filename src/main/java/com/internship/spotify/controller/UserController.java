package com.internship.spotify.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internship.spotify.dto.request.CreateUserRequest;
import com.internship.spotify.dto.request.UpdateUserRequest;
import com.internship.spotify.dto.response.UserResponse;
import com.internship.spotify.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		// Java’da generic"<>" bir tip, içine hangi tipte veri saklayacağını parametre
		// olarak belirleyebileceğiniz bir sınıf
		List<UserResponse> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		Optional<UserResponse> user = userService.getUserById(id);
		// isPresent() Optional içinde değer var mı?

		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {

		try {
			UserResponse createdUser = userService.createUser(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {

		try {
			UserResponse updatedUser = userService.updateUser(id, request);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {

		userService.deleteUser(id);
		return ResponseEntity.ok().build();
		/*
		 * try { userService.deleteUser(id); return
		 * ResponseEntity.ok("Kullanıcı başarıyla silindi."); }catch (RuntimeException
		 * e) { return
		 * ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı."); }
		 */
	}

}
