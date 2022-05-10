package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication
@RequiredArgsConstructor
public class GameClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameClubApplication.class, args);
    }

}
