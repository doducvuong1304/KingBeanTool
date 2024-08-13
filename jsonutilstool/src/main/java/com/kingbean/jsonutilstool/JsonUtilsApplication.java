package com.kingbean.jsonutilstool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class JsonUtilsApplication {
	public static void main(String[] args) {
		SpringApplication.run(JsonUtilsApplication.class, args);
		Logger.getLogger(JsonUtilsApplication.class.getName()).info("Json Utils Application started");
	}
}
