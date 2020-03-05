package com.fussentech.shoporders.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fussentech.shoporders.dao.UserRepository;
import com.fussentech.shoporders.model.User;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository repo;
	
	public UserServiceImpl(UserRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<User> findAll() {
		return repo.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public User save(User user) {
		return repo.save(user);
	}

	@Override
	public Optional<User> update(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (op.isPresent()) {
			User u = op.get();
			u = user;
			// save
			return Optional.of(u);
		}
		return Optional.empty();
	}

	@Override
	public void delete(User user) {
		Optional<User> op = repo.findById(user.getId());
		if (op.isPresent()) {
			User u = op.get();
			if (u == user) {
				repo.delete(user);
			}
		}
	}

}
