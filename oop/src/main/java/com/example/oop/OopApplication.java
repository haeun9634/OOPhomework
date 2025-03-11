package com.example.oop;

import com.example.oop.gui.MemberApp;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopApplication.class, args);
		Application.launch(MemberApp.class, args);
	}

}
