package com.example.mydiaryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyDiaryAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyDiaryAppApplication.class, args);
    }

}
