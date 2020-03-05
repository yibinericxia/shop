package com.fussentech.shoporders.model;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private Long id;
	@NotNull
	@NotEmpty
	private String firstname;
	@NotNull
	@NotEmpty
	private String lastname;
	@NotNull
	@NotEmpty
	private String email;
	private String timeZone;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		User that = (User)obj;
		return Objects.equals(this.getId(), that.getId())
			&& Objects.equals(this.getEmail(), that.getEmail())
			&& Objects.equals(this.getFirstname(), that.getFirstname())
			&& Objects.equals(this.getLastname(), that.getLastname());
//			&& Objects.equals(this.getTimeZone(), that.getTimeZone());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, email, firstname, lastname);
	}
	
	@Override
	public String toString() {
		return "{ \"id\": " + id 
				+ ", \"firstname\": \"" + firstname + "\""
				+ ", \"lastname\": \"" + lastname + "\""
				+ ", \"email\": \"" + email + "\""
				+ "}";
	}
}
