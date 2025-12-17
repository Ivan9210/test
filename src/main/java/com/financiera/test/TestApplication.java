package com.financiera.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScans(value = { @ComponentScan(basePackages={"com.financiera", "org.springdoc"}) })
@EnableJpaRepositories(basePackages = {"com.financiera.repository"})
@EntityScan(basePackages = {"com.financiera.model"})
public class TestApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(TestApplication.class, args);
		
	}

}
