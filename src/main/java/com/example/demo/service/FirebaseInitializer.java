package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseInitializer {
		
	@PostConstruct
	private void initFB() throws IOException{
		
		Resource rs = new ClassPathResource("./financeapp-upc-firebase-adminsdk-142hc-1e06a84899.json");
		
		InputStream serviceaccount = rs.getInputStream();
		
		System.out.println(serviceaccount);
		
		FirebaseOptions options = new FirebaseOptions.Builder()
				  .setCredentials(GoogleCredentials.fromStream(serviceaccount))
				  .build();

		FirebaseApp.initializeApp(options);
	}
	
	@PreDestroy
	private void endFB(){
		FirebaseApp.getInstance().delete();
	}
		
	public Firestore getFireStore() {
		return FirestoreClient.getFirestore();
	}
	
	public FirebaseAuth getFireAtuth() {	
		return FirebaseAuth.getInstance();
	}
}
