package com.fussentech.shopproducts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
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
		String pkg = "com.fussentech.shopproducts.controller";
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(pkg))
				.paths(PathSelectors.regex("/api.*"))
				.build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
	    return new ApiInfoBuilder()
	    		.title("SpringShopProducts")
	    		.description("Spring Shop Products Application")
	            .license("GNU General Public License")
	            .version("1.0")
	            .build();
	}
}
