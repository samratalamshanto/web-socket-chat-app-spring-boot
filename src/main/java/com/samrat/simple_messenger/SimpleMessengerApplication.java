package com.samrat.simple_messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SimpleMessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleMessengerApplication.class, args);
	}

}
