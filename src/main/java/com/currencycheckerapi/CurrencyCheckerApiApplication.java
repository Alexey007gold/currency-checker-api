package com.currencycheckerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyCheckerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyCheckerApiApplication.class, args);
	}

}
