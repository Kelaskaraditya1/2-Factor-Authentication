package com.starkIndustries.RoleBasedAuthorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RoleBasedAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoleBasedAuthorizationApplication.class, args);
	}

}
