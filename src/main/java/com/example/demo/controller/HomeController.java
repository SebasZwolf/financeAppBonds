package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

@RestController
@CrossOrigin
public class HomeController {
	@Autowired
	FirebaseInitializer fi;
	
	@GetMapping("/user")
	public Object checkEmail(@RequestParam(name = "uname", required = true) String uname) throws InterruptedException, ExecutionException{

		System.out.println(uname);
		try {
			fi.getFireAtuth().getUserByEmail(uname);
			return true;
		} catch (FirebaseAuthException e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}

	@GetMapping("/users")
	public List<Object> getAll() throws InterruptedException, ExecutionException {
		List<Object> usersS = new ArrayList<>();
		
		CollectionReference users = fi.getFireStore().collection("users");
		
		//REQUEST
		ApiFuture<QuerySnapshot> qS = users.get();

		for(DocumentSnapshot doc:qS.get().getDocuments()) {
			System.out.println(doc.getId());
			System.out.println(doc.getData());
			usersS.add(doc.getData());
		}
		return usersS;
	}
	
	@GetMapping("/users/{id}")
	public Object getOne(@PathVariable(name = "id", required = true) String id) throws InterruptedException, ExecutionException {
		
		DocumentReference user = fi.getFireStore().collection("users").document(id);
		
		//REQUEST
		ApiFuture<DocumentSnapshot> future = user.get();
		
		DocumentSnapshot document = future.get();
		
		if (document.exists()) {
			return document.getData();
			} else {
			return null;
			}
	}

	@PostMapping("/users/{id}")
	public Object postOne(@PathVariable(name = "id", required = true) String id, @RequestBody Object object) throws InterruptedException, ExecutionException, IOException {
		
		DocumentReference user = fi.getFireStore().collection("users").document(id);
		System.out.println(user.set(object).get());


		user = fi.getFireStore().collection("users").document(id);
		ApiFuture<DocumentSnapshot> future = user.get();
		DocumentSnapshot document = future.get();
		
		return document.getData();
	}
	
	@PostMapping("/register")
	public Object register() {
		return null;
	}
}
