package com.nebiyu.Kelal;

import com.nebiyu.Kelal.model.User;
import com.nebiyu.Kelal.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class KelalApplication {
	@Autowired
	private UserRepository userRepository;
@PostConstruct
//	public void initUser(){
//		List<User> users = Stream.of(
//				new User(1L, "Nebiyu", "Takele","nebiyu28@gmail.com", "123456", "user" ),
//		new User(1222, "neo", "take", "","" ,""),
//				new User(1L, "Nebiyu", " Takele", "nebiyu28@gmail.com" ,"123456", "user"),
//				new User(1L, "Nebiyu", " Takele", "nebiyu28@gmail.com" ,"123456", "user"),
//				new User(1L, "Nebiyu", " Takele", "nebiyu28@gmail.com" ,"123456", "user"),
//				new User(1L, "Nebiyu", " Takele", "nebiyu28@gmail.com" ,"123456", "user"),
//				new User(1L, "Nebiyu", " Takele", "nebiyu28@gmail.com" ,"123456", "user")
//
//
//		).collect(Collectors.toList());
//		userRepository.saveAll(users);
//	}

	public static void main(String[] args) {
		SpringApplication.run(KelalApplication.class, args);
	}

}
