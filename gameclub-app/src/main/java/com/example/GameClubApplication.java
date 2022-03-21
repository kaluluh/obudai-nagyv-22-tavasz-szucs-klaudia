package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class GameClubApplication implements CommandLineRunner {

    private final GameClubService gameClubService;

    public static void main(String[] args) {
       SpringApplication.run(GameClubApplication.class, args);
    }

    @Override
    public void run(String... args) {
        gameClubService.loadData();
        gameClubService.initialize();
    }
}
