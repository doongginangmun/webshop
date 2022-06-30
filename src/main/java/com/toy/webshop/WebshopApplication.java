package com.toy.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class WebshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebshopApplication.class, args);
	}

}
