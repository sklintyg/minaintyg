package se.inera.intyg.minaintyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@SpringBootApplication
public class MinaintygApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinaintygApplication.class, args);
    }
}
