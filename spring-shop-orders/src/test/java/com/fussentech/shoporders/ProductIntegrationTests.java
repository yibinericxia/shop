package com.fussentech.shoporders;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

//import static org.hamcrest.Matchers.closeTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.fussentech.shoporders.controller.ProductController;
import com.fussentech.shoporders.model.Product;
import com.fussentech.shoporders.service.ProductService;

//Use the real profile Mongo DB for this testing
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ProductIntegrationTests {

	@LocalServerPort
	private int port;
	
	@Mock
	private ProductService productService;
	@InjectMocks
	private ProductController productController;
	
	private Product product8;
	
    @BeforeAll
    public void init() {
    	// need to use @TestInstance(Lifecycle.PER_CLASS)
        baseURI = "http://localhost:" + port;
        
		// the 8th record as the real db has no such a record
        product8 = new Product();
		product8.setId(8L);
		product8.setName("Foo");
		product8.setPrice(BigDecimal.valueOf(100.0));
		product8.setMaker("dummy");
    }
    
	@BeforeEach
	@Test
	public void test_deleteProduct_NotFound() {
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product8)
			.when()
			.delete("/api/products")
			.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			;
	}

	@Test
	public void test_createProduct_Created() {
		Product retval = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product8)
			.when()
			.post("/api/products")
			.then()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.as(Product.class)
			;
		assertThat(retval).isEqualTo(product8);
	}
	
	@Test
	public void test_getAllProducts_OK() {
        get("/api/products")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("content.size()", greaterThan(3))
			.body(containsString("TV"))
	//		.body("find {it.name == 'Sofa'}.price", closeTo(1500, 0.1))
			.body("name", hasItems("iPhone", "Sofa", "TV"))
			.body("maker", hasItems("Costco", "apple"))
			;
	}

	@Test
	public void test_getProductsByMaker_OK() {
		String maker = "apple";
		get("/api/products?maker=" + maker)
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", hasItems("iPhone"))
			.body("content.size()", is(2))
			;
	}

	@Test
	public void test_getProductById_OK() {
		Long id = 2L;
		get("/api/products/id/" + id)
			.then()
			.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("name", equalTo("Sofa"))
			.body("maker", equalTo("Costco"))
//			.body("price", is(closeTo(500, 0.0)))
			;
	}

	@Test
	public void test_getProductById_NotFound() {
		Long id = 0L;
		get("/api/products/id/" + id)
			.then()
			.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}

	@Test
	public void test_createProduct_Conflict() {
        Product product = new Product();
		product.setId(2L);
		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.statusCode(HttpStatus.CONFLICT.value())
			.body(containsString("already exists"))
			;
	}
	
	@Test
	public void test_createProduct_withoutName() {
		Product product = new Product();
		product.setId(111L);
//		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body(containsString("must not be"))
			;
	}
	
	@Test
	public void test_createProduct_withoutPrice() {
		Product product = new Product();
		product.setId(1000L);
		product.setName("foo");
//		product.setPrice(BigDecimal.valueOf(100.0"));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body(containsString("must not be null"))
			;
	}
	
	@Test
	public void test_updateProduct_OK() {
		Product product = new Product();
		product.setId(6L);
		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		Product retval = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.put("/api/products")
			.then()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.as(Product.class)
			;
		assertThat(retval).isEqualTo(product);
	}
	
	@Test
	public void test_updateProduct_NotFound() {
		Product product = new Product();
		product.setId(1000L);
		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.put("/api/products")
			.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}
	
}