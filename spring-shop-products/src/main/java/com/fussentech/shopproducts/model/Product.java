package com.fussentech.shopproducts.model;

import java.math.BigDecimal;
import java.util.List;
//import java.util.List;
import java.util.Objects;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
/*
import java.util.UUID;

import javax.persistence.Column;
import org.hibernate.annotations.Type;
*/
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	@Column(unique = true)
//	@Type(type="org.hibernate.type.UUIDCharType")
//	private UUID uuid;
	@NotNull
	@NotEmpty
	private String name;
	private String maker;
	@ElementCollection
	private List<String> category;
	private String description;
	@NotNull
	private BigDecimal price;
	private String imageUrl;
	
	public Product() {
		
	}
	
	public Product(String name, BigDecimal price, String maker) {
		this.name = name;
		this.price = price;
		this.maker = maker;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/*
	public UUID getUUID() {
		return uuid;
	}
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Product that = (Product)obj;
		return Objects.equals(this.getId(), that.getId())
			&& Objects.equals(this.getName(), that.getName())
			&& Objects.equals(this.getMaker(), that.getMaker())
			&& Objects.equals(this.getCategory(), that.getCategory())
			&& Objects.equals(this.getDescription(), that.getDescription())
			&& Objects.equals(this.getPrice(), that.getPrice())
			&& Objects.equals(this.getImageUrl(), that.getImageUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, maker);
	}
	@Override
	public String toString() {
		return "{ \"id\": " + id 
				+ ", \"name\": \"" + name + "\""
				+ ", \"maker\": \"" + maker + "\""
				+ ", \"price\": " + price 
				+ "}";
	}

}
