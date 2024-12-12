package com.fussentech.shopcustomers.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fussentech.shopcustomers.exception.UserException;
import com.fussentech.shopcustomers.model.User;
import com.fussentech.shopcustomers.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.getUsers();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/users/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		Optional<User> op = userService.findByEmail(email);
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, email + " not found");
		}
		User user = op.get();
		return ResponseEntity.ok(user);
	}

	@GetMapping("/users/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> op = userService.findById(id);
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, "id " + id + " not found");
		}
		User user = op.get();
		return ResponseEntity.ok(user);
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		Optional<User> op = userService.findById(user.getId());
		if (op.isPresent()) {
			throw new UserException(HttpStatus.CONFLICT, "id " + user.getId() + " already exists");
		}
		op = userService.findByEmail(user.getEmail());
		if (op.isPresent()) {
			throw new UserException(HttpStatus.CONFLICT, "email " + user.getEmail() + " already exists");
		}
		User savedUser = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}
	
	@PutMapping("/users")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
		Optional<User> op = userService.updateUser(user);
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, "id " + user.getId() + " not found");
		}
		User updatedUser = op.get();
		return ResponseEntity.ok(updatedUser);
	}
	
	@DeleteMapping("/users")
	public ResponseEntity<Void> deleteUser(@RequestBody User user) {
		Optional<User> op = userService.findById(user.getId());
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, "id " + user.getId() + " not found");
		}
		userService.deleteUser(user);
		return ResponseEntity.noContent().build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	private Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> map = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String field = ((FieldError) error).getField();
			String msg = error.getDefaultMessage();
			map.put(field, msg);
		});
		return map;
	}
}
