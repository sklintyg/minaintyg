package se.inera.intyg.minaintyg.integration.webcert.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebcertIntegrationConfig {

  @Bean(name = "webcertWebClient")
  public WebClient webClientForIntygstjanst() {
    return WebClient.builder()
        .build();
  }
}
