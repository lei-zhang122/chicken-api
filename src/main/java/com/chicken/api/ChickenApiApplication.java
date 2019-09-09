package com.chicken.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAsync
public class ChickenApiApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ChickenApiApplication.class, args);
    }
}
