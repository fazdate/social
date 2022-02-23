package com.fazdate.social;

import com.fazdate.social.helpers.DatabaseRecreater;
import com.fazdate.social.services.firebaseServices.MainService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class SocialApplication {
	public static void main(String[] args) throws IOException, ExecutionException, FirebaseAuthException, InterruptedException {
		MainService.initFirebase();
		// DatabaseRecreater.recreateDatabase();
		SpringApplication.run(SocialApplication.class, args);
	}
}
