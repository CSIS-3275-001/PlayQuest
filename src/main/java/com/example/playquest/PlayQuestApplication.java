package com.example.playquest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class PlayQuestApplication {

    public static void main(String[] args) {

        SpringApplication.run(PlayQuestApplication.class, args);
    }

}
