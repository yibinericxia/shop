package com.fussentech.shopcustomers.service;

import java.util.List;
import java.util.Optional;

import com.fussentech.shopcustomers.model.User;

public interface UserService {
	List<User> getUsers();
	Optional<User> findById(Long id);
	Optional<User> findByEmail(String email);
	User createUser(User user);
	Optional<User> updateUser(User user);
	void deleteUser(User user);
}
