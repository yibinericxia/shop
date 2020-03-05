package com.fussentech.shopcustomers.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		String pkg = "com.fussentech.shopcustomers.controller";
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(pkg))
				.paths(PathSelectors.regex("/api.*"))
				.build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
	    return new ApiInfo(
	            "SpringShopCustomers",
	            "Spring Shop Customerss Application",
	            "1.0",
	            null,
	            null,
	            "GNU General Public License",
	            null,
	            Collections.emptyList()
	    );
	}
}
