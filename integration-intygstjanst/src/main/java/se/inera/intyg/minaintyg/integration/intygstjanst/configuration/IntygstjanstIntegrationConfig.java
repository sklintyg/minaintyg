package se.inera.intyg.minaintyg.integration.intygstjanst.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IntygstjanstIntegrationConfig {

    @Bean(name = "intygstjanstWebClient")
    public WebClient webClientForIntygstjanst() {
        return WebClient.builder()
            .build();
    }
}
