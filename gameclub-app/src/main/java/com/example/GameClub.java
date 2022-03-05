package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameClub {
    public static void main(String[] args) {
       SpringApplication.run(GameClub.class, args);
       System.out.println("hello szia mukodik?");
    }
}
