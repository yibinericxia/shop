package com.fussentech.shoporders.model;

import java.math.BigDecimal;

public class OrderedProduct {

	private Product product;
	private Integer quantity;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalCost() {
		return product.getPrice().multiply(BigDecimal.valueOf(quantity));
	}
	
	@Override
	public String toString() {
		return "product: " + product + ", quantity: " + quantity;
	}
}
