package com.fussentech.shoporders.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fussentech.shoporders.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
	
}
