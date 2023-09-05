package se.inera.intyg.minaintyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class MinaintygApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinaintygApplication.class, args);
    }
}
