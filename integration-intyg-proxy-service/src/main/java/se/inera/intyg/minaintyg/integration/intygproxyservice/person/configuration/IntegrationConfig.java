package se.inera.intyg.minaintyg.integration.intygproxyservice.person.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.common.ExchangeFilterFunctionProvider;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

  private final ExchangeFilterFunctionProvider exchangeFilterFunctionProvider;

  @Bean(name = "intygProxyWebClient")
  public WebClient webClientForIntygProxy() {
    return WebClient.builder()
        .filter(exchangeFilterFunctionProvider.addHeadersFromMDCToRequest())
        .build();
  }
}
