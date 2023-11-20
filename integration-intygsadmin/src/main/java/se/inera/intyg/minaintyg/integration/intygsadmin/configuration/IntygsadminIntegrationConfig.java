package se.inera.intyg.minaintyg.integration.intygsadmin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.common.ExchangeFilterFunctionProvider;

@Configuration
public class IntygsadminIntegrationConfig {

  @Bean(name = "intygsadminWebClient")
  public WebClient webClientForIntygstjanst() {
    return WebClient.builder()
        .filter(ExchangeFilterFunctionProvider.addHeadersFromMDCToRequest())
        .build();
  }
}
