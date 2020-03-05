package com.fussentech.shoporders.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.OrderStatus;
import com.fussentech.shoporders.model.OrderedProduct;
import com.fussentech.shoporders.model.Product;

class OrderDTOTests {

	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Test
	void test_Order2OrderDTO() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setPrice(BigDecimal.valueOf(800.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		Order order = new Order();
		order.setStatus(OrderStatus.CREATED);
		order.setOrderedProducts(list);
		order.setCreatedTime(LocalDateTime.now());
		
		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
		assertEquals(order.getOrderedProducts(), orderDTO.getOrderedProducts());
		assertEquals(order.getCreatedTime(), orderDTO.getCreatedTime());
	}

	@Test
	void test_OrderDTO2Order() {
		Product product = new Product();
		product.setId(1L);
		product.setName("TV");
		product.setPrice(BigDecimal.valueOf(800.0));
		OrderedProduct ordered = new OrderedProduct();
		ordered.setProduct(product);
		ordered.setQuantity(2);
		List<OrderedProduct> list = new ArrayList<>();
		list.add(ordered);
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderedProducts(list);
		orderDTO.setCreatedTime(LocalDateTime.now());
		
		Order order = modelMapper.map(orderDTO, Order.class);
		assertEquals(order.getOrderedProducts(), orderDTO.getOrderedProducts());
		assertEquals(order.getCreatedTime(), orderDTO.getCreatedTime());
	}

}
