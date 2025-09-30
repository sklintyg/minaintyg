package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.configuration;

import static se.inera.intyg.minaintyg.integration.api.citizen.CitizenConstants.CITIZEN_IPS_INTEGRATION;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.common.ExchangeFilterFunctionProvider;

@Configuration
@RequiredArgsConstructor
@Profile(CITIZEN_IPS_INTEGRATION)
public class IntegrationConfig {

  @Bean(name = "intygProxyCitizenWebClient")
  public WebClient webClientForIntygProxy() {
    return WebClient.builder()
        .filter(ExchangeFilterFunctionProvider.addHeadersFromMDCToRequest())
        .build();
  }
}