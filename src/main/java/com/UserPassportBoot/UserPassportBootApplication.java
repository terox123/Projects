package com.UserPassportBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/*@EntityScan(basePackages = "com.UserPassportBootApplication.model")
@EnableJpaRepositories(basePackages = "com.UserPassportBoot.repositories")*/
public class UserPassportBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserPassportBootApplication.class, args);
	}

}
