package com.fussentech.shoporders.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fussentech.shoporders.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
	
	List<Order> findByUserId(Long userId);
}
