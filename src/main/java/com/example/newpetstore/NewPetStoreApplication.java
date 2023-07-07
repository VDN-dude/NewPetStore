package com.example.newpetstore;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
public class NewPetStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewPetStoreApplication.class, args);
    }


}
