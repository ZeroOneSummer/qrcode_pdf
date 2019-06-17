package com.tencet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Project thymeleaf-archetype.
 * Package: PACKAGE_NAME
 * Description: 描述
 * Author: ZeroOneSummer
 * Date: 2019年04月15日 21:20
 */
@SpringBootApplication
public class AppWeb implements ApplicationListener<ContextRefreshedEvent>{

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppWeb.class);
//        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("The app is started .");
    }

}
