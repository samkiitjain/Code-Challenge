package dk.tryg.code.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "dk.tryg.code.service")
@EntityScan(basePackages = "dk.tryg.code.model")
@ComponentScan(basePackages = {"dk.tryg.code.controller", "dk.tryg.code.service"})
public class Challenge {
    public static void main(String[] args) {
        SpringApplication.run(Challenge.class, args);
    }
}
