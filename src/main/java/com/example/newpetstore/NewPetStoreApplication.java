package com.example.newpetstore;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
public class NewPetStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewPetStoreApplication.class, args);
    }


}
