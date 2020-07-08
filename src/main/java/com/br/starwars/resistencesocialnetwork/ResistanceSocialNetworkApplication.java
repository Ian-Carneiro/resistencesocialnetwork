package com.br.starwars.resistencesocialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
public class ResistanceSocialNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResistanceSocialNetworkApplication.class, args);
    }

}
