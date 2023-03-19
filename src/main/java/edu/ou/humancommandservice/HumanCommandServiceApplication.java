package edu.ou.humancommandservice;

import edu.ou.coreservice.annotation.BaseCommandAnnotation;
import org.springframework.boot.SpringApplication;


@BaseCommandAnnotation
public class HumanCommandServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanCommandServiceApplication.class, args);
    }

}
