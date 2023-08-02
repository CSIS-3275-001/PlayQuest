package com.example.playquest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableConfigurationProperties
@SpringBootApplication
@EnableJpaAuditing
@Import(AppConfig.class)
public class PlayQuestApplication {

    public static void main(String[] args) {

        SpringApplication.run(PlayQuestApplication.class, args);
    }

}
