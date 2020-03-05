package com.fussentech.shoporders.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fussentech.shoporders.model.OrderedProduct;

public class OrderDTO {

	@NotNull
	@NotEmpty
	private List<OrderedProduct> orderedProducts;
	@NotNull
	private LocalDateTime createdTime;
//	private UserDTO userDTO;
	
	public List<OrderedProduct> getOrderedProducts() {
		return orderedProducts;
	}
	
	public void setOrderedProducts(List<OrderedProduct> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	
	/*
	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	*/
}
