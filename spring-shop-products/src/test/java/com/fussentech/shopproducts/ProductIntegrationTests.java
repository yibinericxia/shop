package com.fussentech.shopproducts;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.fussentech.shopproducts.model.Product;
import com.fussentech.shopproducts.service.ProductService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ProductIntegrationTests {

	@LocalServerPort
    private int port;
	
	@Autowired
	private ProductService productService;
	
    @BeforeAll
    public void init() {
    	// need to use @TestInstance(Lifecycle.PER_CLASS)
        baseURI = "http://localhost:" + port;
        
		productService.save(new Product("TV", BigDecimal.valueOf(500), "Samsung"));
		productService.save(new Product("Sofa", BigDecimal.valueOf(1500), "Costco"));
		productService.save(new Product("iPhone", BigDecimal.valueOf(800), "apple"));
		productService.save(new Product("Laptop", BigDecimal.valueOf(700), "HP"));
		productService.save(new Product("watch", BigDecimal.valueOf(100), "seiko"));
		productService.save(new Product("Beer", BigDecimal.valueOf(9), "China"));
		productService.save(new Product("foo", BigDecimal.valueOf(500), "apple"));
   }

	@Test
	public void test_getAllProducts_OK() {
		get("/api/products")
		.then()
		.assertThat()
		.statusCode(HttpStatus.OK.value())
		.body("content.size()", greaterThan(3))
		.body(containsString("TV"))
		.body("find {it.name == 'Sofa'}.price", equalTo(1500f))
		.body("name", hasItems("iPhone", "Sofa", "TV"))
		.body("maker", hasItems("Costco", "apple"))
		;
	}

	@Test
	public void test_getProductsByMaker_OK() {
		String maker = "apple";
		get("/api/products/maker/" + maker)
			.then()
			.assertThat()
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
			.body("price", equalTo(1500f))
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
	public void test_createProduct_Created() {
		Product product = new Product();
		// the 8th record after init()
		product.setId(8L);
		product.setName("Foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		Product retval = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.assertThat()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.as(Product.class);
		assertThat(retval).isEqualTo(product);
	}
	
	@Test
	public void test_createProduct_Conflict() {
		Product product = new Product();
		product.setId(1L);
		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.assertThat()
			.statusCode(HttpStatus.CONFLICT.value())
			.body(containsString("already exists"))
			;
	}
	
	@Test
	public void test_createProduct_withoutName() {
		Product product = new Product();
		product.setId(1000L);
//		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body(containsString("must not be"))
			;
	}
	
	@Test
	public void test_createProduct_withoutPrice() {
		Product product = new Product();
		product.setId(1000L);
		product.setName("foo");
//		product.setPrice(100.0);
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then()
			.assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body(containsString("must not be null"))
			;
	}
	
	@Test
	public void test_updateProduct_OK() {
		Product product = new Product();
		product.setId(1L);
		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		Product retval = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.put("/api/products")
			.then()
			.assertThat()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.as(Product.class);
		assertThat(retval).isEqualTo(product);
	}
	
	@Test
	public void test_updateProduct_BadRequest() {
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
			.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}
	
	@Test
	public void test_deleteProduct_OK() {
		Product product = new Product();
		product.setId(1L);
		product.setName("foo");
		product.setPrice(BigDecimal.valueOf(100.0));
		product.setMaker("dummy");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.delete("/api/products")
			.then()
			.assertThat()
			.statusCode(HttpStatus.NO_CONTENT.value())
			;
	}
}