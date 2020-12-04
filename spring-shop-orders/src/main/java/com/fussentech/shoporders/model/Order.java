package com.fussentech.shoporders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {
	@Id
	private String id;
	private User user;
	@NotNull(message = "products must not be null")
	@NotEmpty
	private List<OrderedProduct> orderedProducts;
	private LocalDateTime createdTime;
	private OrderStatus status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderedProduct> getOrderedProducts() {
		return orderedProducts;
	}
	public void setOrderedProducts(List<OrderedProduct> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	@Transient
	public BigDecimal getTotalCost() {
		BigDecimal totalCost = BigDecimal.ZERO;
		for (OrderedProduct p : orderedProducts ) {
			totalCost = totalCost.add(p.getTotalCost());
		}
		return totalCost;
	}
	@Transient
	public int getNumberOfProducts() {
		int count = 0;
		for (OrderedProduct p : orderedProducts ) {
			count += p.getQuantity();
		}
		return count;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, user, orderedProducts, createdTime, status);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Order that = (Order) obj;
		return Objects.equals(this.id, that.id)
			&& Objects.equals(this.user, that.user)
			&& Objects.equals(this.orderedProducts, that.orderedProducts)
			&& Objects.equals(this.createdTime, that.createdTime)
			&& Objects.equals(this.status, that.status);
	}
	
	@Override
	public String toString() {
		return "{ \"id\": " + id 
				+ ", \"user\": " + user 
				+ ", \"orderedProducts\": " + orderedProducts
				+ ", \"totalCost\": " + getTotalCost() 
				+ ", \"status\": " + status
				+ "}";
	}
}
