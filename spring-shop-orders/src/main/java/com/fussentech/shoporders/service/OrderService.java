package com.fussentech.shoporders.service;

import java.util.List;
import java.util.Optional;

import com.fussentech.shoporders.model.Order;

public interface OrderService {
	List<Order> findAll();
	Optional<Order> findById(String id);
	Order save(Order order);
}
