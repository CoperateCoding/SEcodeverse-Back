package com.coperatecoding.secodeverseback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class SEcodeverseBackApplication {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String ldtStr = now.format(dtFmt);
        System.out.println(ldtStr);
        SpringApplication.run(SEcodeverseBackApplication.class, args);
    }

}
