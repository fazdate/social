package com.fazdate.social;

import com.fazdate.social.services.firebaseServices.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {
	public static void main(String[] args) throws IOException {
		MainService.initFirebase();
		SpringApplication.run(Application.class, args);
	}
}
