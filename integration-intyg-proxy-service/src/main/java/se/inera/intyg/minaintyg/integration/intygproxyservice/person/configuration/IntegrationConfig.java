package se.inera.intyg.minaintyg.integration.intygproxyservice.person.configuration;

import static se.inera.intyg.minaintyg.integration.api.ExchangeFilterFunctionProvider.getExchangeFilterFunction;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

  @Bean(name = "intygProxyWebClient")
  public WebClient webClientForIntygProxy() {
    return WebClient.builder()
        .filter(getExchangeFilterFunction())
        .build();
  }
}
