package com.example.vse_site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VseSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(VseSiteApplication.class, args);
    }

}
