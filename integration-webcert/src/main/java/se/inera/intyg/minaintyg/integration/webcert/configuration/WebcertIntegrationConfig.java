package se.inera.intyg.minaintyg.integration.webcert.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.common.ExchangeFilterFunctionProvider;

@Configuration
@RequiredArgsConstructor
public class WebcertIntegrationConfig {

  private final ExchangeFilterFunctionProvider exchangeFilterFunctionProvider;

  @Bean(name = "webcertWebClient")
  public WebClient webClientForWebcert() {
    return WebClient.builder()
        .filter(exchangeFilterFunctionProvider.addHeadersFromMDCToRequest())
        .build();
  }
}
