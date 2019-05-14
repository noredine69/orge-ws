package com.noredine69.orge.ws.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.noredine69.orge.ws.*")
@EnableSwagger2
public class SprintBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprintBootApplication.class, args);
    }
}
