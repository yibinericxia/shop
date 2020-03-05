package com.fussentech.shoporders.service;

public interface ProductEventHandler {

	void handleProductCreation(String productStr);
	void handleProductUpdate(String productStr);
	void handleProductDeletion(String productStr);
}
