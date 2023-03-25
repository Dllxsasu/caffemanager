package com.jeremias.dev.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;




@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.jeremias.dev"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiEndPointsInfo());
    }
	private ApiKey apiKey() {
	    return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
	    return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	    authorizationScopes[0] = authorizationScope;
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
    private ApiInfo apiEndPointsInfo() {

        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Language Management REST API")
                .contact("enn el camino me perdi por ahi")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0-SNAPSHOT")
                .build();
    }
	/*
	 @Bean
	    public OpenAPI baseOpenAPI() {
	    
	     return new OpenAPI()
	                .info(getApiInfo());
	    }
	 
	    private Info getApiInfo() {
	        return new Info()
	                .title("API for management cafe")
	                .version("1.0")
	                .description("API for el cafesito de sasu");
	                //.contact(new Contact("Sasu dxd ", "https://github.com/Dllxsasu", "s@email.com"))
	             //   .license("Apache License Version 2.0")
	              //  .build();
	    }
*/
	
}
