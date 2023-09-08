package se.inera.intyg.minaintyg.integration.intygproxyservice.person.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IntegrationConfig {

    @Bean(name = "intygProxyWebClient")
    public WebClient webClientForIntygProxy() {
        return WebClient.builder()
            .build();
    }
}
