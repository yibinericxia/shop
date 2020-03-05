package com.fussentech.shoporders.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fussentech.shoporders.dto.OrderDTO;
import com.fussentech.shoporders.exception.OrderException;
import com.fussentech.shoporders.model.Order;
import com.fussentech.shoporders.model.OrderStatus;
import com.fussentech.shoporders.model.OrderedProduct;
import com.fussentech.shoporders.model.User;
import com.fussentech.shoporders.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	private OrderService orderService;
	
	private ModelMapper modelMapper;
	
	public OrderController(OrderService orderService, ModelMapper modelMapper) {
		this.orderService = orderService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping("/orders")
	public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderDTO orderDTO) throws MethodArgumentNotValidException {
		Order order = toOrder(orderDTO);
		Order savedOrder = orderService.save(order);
		if (savedOrder == null) {
			String msg = "order cannot be completed";
			throw new OrderException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
		}
		logger.info("Order created: " + order);
//		return ResponseEntity.created(new URI("/orders/"+order.getId())).body(savedOrder);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
	}
	
	@GetMapping("/orders")
	public ResponseEntity<List<OrderDTO>> getOrderDTOs() {
		List<Order> orders = orderService.findAll();
		List<OrderDTO> orderDTOs = orders.stream()
				.map(this::toOrderDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(orderDTOs);
	}

	@GetMapping("/orders/details")
	public ResponseEntity<List<Order>> getOrders() {
		List<Order> orders = orderService.findAll();
		return ResponseEntity.ok(orders);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> map = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String field = ((FieldError) error).getField();
			String msg = error.getDefaultMessage();
			map.put(field, msg);
		});
		return map;
	}
	
	private Order toOrder(OrderDTO orderDTO) {
		List<OrderedProduct> products = orderDTO.getOrderedProducts();
		if (products == null || products.size() == 0) {
			String msg = "orderedProducts cannot be empty";
			throw new OrderException(HttpStatus.NOT_ACCEPTABLE, msg);
		};
		if (orderDTO.getCreatedTime() == null) {
			String msg = "createdTime cannot be null";
			throw new OrderException(HttpStatus.NOT_ACCEPTABLE, msg);
		}
		Order order = modelMapper.map(orderDTO, Order.class);
		
		User user = new User();
		order.setUser(user);
		order.setStatus(OrderStatus.CREATED);
		return order;
	}
	
	private OrderDTO toOrderDTO(Order order) {
		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
		return orderDTO;
	}
}
