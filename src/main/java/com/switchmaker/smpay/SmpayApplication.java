package com.switchmaker.smpay;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmpayApplication {
	@Bean
	ModelMapper modelMappar() {
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SmpayApplication.class, args);
	}

}
