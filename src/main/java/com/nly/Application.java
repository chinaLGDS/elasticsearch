package com.nly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication

public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class,args);
    }
    //ApplicationListener
}