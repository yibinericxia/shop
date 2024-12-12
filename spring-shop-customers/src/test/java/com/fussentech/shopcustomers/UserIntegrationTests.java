package com.fussentech.shopcustomers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.fussentech.shopcustomers.model.User;
import com.fussentech.shopcustomers.service.UserService;

//import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

//import javax.annotation.PostConstruct;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UserIntegrationTests {
    
	@Autowired
	private UserService service;
	
	@LocalServerPort
    private int port;

    /*
    private String uri;
    
    @PostConstruct
    public void init() {
    	// all the tests need to add this uri before endpoint in get/put/post/delete
        uri = "http://localhost:" + port;
    }
	*/
    @BeforeAll
    public void init() {
    	// need to use @TestInstance(Lifecycle.PER_CLASS)
        baseURI = "http://localhost:" + port;
        
        service.createUser(new User(1L, "Derek", "Lee", "derek.lee@gmail.com"));
        service.createUser(new User(2L, "john", "smith", "john.smith@gmail.com"));
        service.createUser(new User(3L, "cathy", "gates", "cathy.gates@gmail.com"));
        service.createUser(new User(4L, "david", "johnson", "dj@gmail.com"));
    }

	@Test
	public void getAllUsers_OK() {
		/*
		ValidatableResponse res = 
			given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().
		*/
			get("/api/users")
				.then()
		/*
				.then();

		System.out.println("********" + res.extract().asString());
		
		res
		*/
			.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("content.size()", greaterThan(3))
			.body(containsString("derek"))
			.body("find {it.email == 'john.smith@gmail.com'}.firstname", equalTo("john"))
			.body("find {it.email == 'john.smith@gmail.com'}.lastname", equalTo("smith"))
			.body("firstname", hasItems("Derek", "john", "cathy"))
			.body("lastname", hasItems("Lee", "smith"))
			;
	}

	@Test
	public void getUserByEmail_OK() {
		String email = "john.smith@gmail.com";
		get("/api/users/email/" + email)
			.then()
			.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("email", equalTo(email))
			.body("firstname", equalTo("john"))
			.body("lastname", equalTo("smith"))
			;
	}

	@Test
	public void getUserByEmail_NotFound() {
		String email = "foo@gmail.com";
		get("/api/users/email/" + email)
			.then()
			.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}

	@Test
	public void getUserById_OK() {
		Long id = 2L;
		get("/api/users/id/" + id)
			.then()
			.assertThat()
			.statusCode(HttpStatus.OK.value())
			.body("firstname", equalTo("john"))
			.body("lastname", equalTo("smith"))
			.body("email", equalTo("john.smith@gmail.com"))
			;
	}

	@Test
	public void getUserById_NotFound() {
		Long id = 0L;
		get("/api/users/id/" + id)
			.then()
			.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}

	@Test
	public void createUser_Created() {
		User user = new User();
		// the 5th record after init()
		user.setId(5L);
		user.setFirstname("Foo");
		user.setLastname("Lau");
		user.setEmail("foo.lau@example.com");
		
		User retval = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(user)
			.when()
			.post("/api/users")
			.then()
			.assertThat()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.as(User.class);
		assertThat(retval).isEqualTo(user);
	}
	
	@Test
	public void test_createUser_Conflict() {
		User user = new User();
		user.setId(1L);
		user.setFirstname("Foo");
		user.setLastname("Lau");
		user.setEmail("foo.lau@example.com");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(user)
			.when()
			.post("/api/users")
			.then()
			.assertThat()
			.statusCode(HttpStatus.CONFLICT.value())
			.body(containsString("already exists"))
			;
	}
	
	@Test
	public void updateUser_OK() {
		User user = new User();
		user.setId(1L);
		user.setFirstname("Derek");
		user.setLastname("Lee");
		user.setEmail("derek.lee@example.com");
		
		User retval = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(user)
			.when()
			.put("/api/users")
			.then()
			.assertThat()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.as(User.class);
		assertThat(retval).isEqualTo(user);
	}
	
	@Test
	public void updateUser_NotFound() {
		User user = new User();
		user.setId(15L);
		user.setFirstname("Foo");
		user.setLastname("Lau");
		user.setEmail("foo.lau@example.com");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(user)
			.when()
			.put("/api/users")
			.then()
			.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}
	
	@Test
	public void deleteUser_NotFound() {
		User user = new User();
		user.setId(9L);
		user.setFirstname("Foo");
		user.setLastname("Lau");
		user.setEmail("foo.lau@example.com");
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(user)
			.when()
			.delete("/api/users")
			.then()
			.assertThat()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(containsString("not found"))
			;
	}
}
