package com.fussentech.shoporders.service;

public interface UserEventHandler {

	void handleUserCreation(String userStr);
	void handleUserUpdate(String userStr);
	void handleUserDeletion(String userStr);
}
