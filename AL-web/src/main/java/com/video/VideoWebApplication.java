package com.video;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VideoWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoWebApplication.class, args);
    }
}

