package com.fussentech.shopcustomers.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	public User() {
		
	}
	
	public User(Long id, String firstname, String lastname, String email) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}
	
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
		return "id: " + id + " email: " + email 
				+ " firstname: " + firstname + " lastname: " + lastname;
	}
}
