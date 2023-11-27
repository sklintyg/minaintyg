package se.inera.intyg.minaintyg.integration.webcert.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.common.ExchangeFilterFunctionProvider;

@Configuration
@RequiredArgsConstructor
public class WebcertIntegrationConfig {

  private static final int IN_MEMORY_SIZE_TO_MANAGE_LARGE_PDF_RESPONSES = 16 * 1024 * 1024;

  @Bean(name = "webcertWebClient")
  public WebClient webClientForWebcert() {
    return WebClient.builder()
        .codecs(configurer -> configurer.defaultCodecs()
            .maxInMemorySize(IN_MEMORY_SIZE_TO_MANAGE_LARGE_PDF_RESPONSES)
        )
        .filter(ExchangeFilterFunctionProvider.addHeadersFromMDCToRequest())
        .build();
  }
}
