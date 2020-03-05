package com.fussentech.shoporders.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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

import com.fussentech.shoporders.exception.UserException;
import com.fussentech.shoporders.model.User;
import com.fussentech.shoporders.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	private UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = service.findAll();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/users/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> op = service.findById(id);
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, "id " + id + " not found");
		}
		User user = op.get();
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> save(@Valid @RequestBody User user) {
		Optional<User> op = service.findById(user.getId());
		if (op.isPresent()) {
			throw new UserException(HttpStatus.CONFLICT, "id " + user.getId() + " already exists");
		}
		User saved = service.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/users")
	public ResponseEntity<User> update(@Valid @RequestBody User user) {
		Optional<User> op = service.update(user);
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, "user not found");
		}
		User updated = op.get();
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/users")
	public ResponseEntity<Void> delete(@RequestBody User user) {
		Optional<User> op = service.findById(user.getId());
		if (!op.isPresent()) {
			throw new UserException(HttpStatus.NOT_FOUND, "user not found");
		}
		service.delete(user);
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
