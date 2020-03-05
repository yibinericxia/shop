package com.fussentech.shoporders.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fussentech.shoporders.model.User;

public interface UserService {

	List<User> findAll();
	Optional<User> findById(Long id);
	User save(@Valid User user);
	Optional<User> update(User user);
	void delete(User user);

}
