package se.inera.intyg.minaintyg.integration.intygstjanst.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.common.ExchangeFilterFunctionProvider;

@Configuration
@RequiredArgsConstructor
public class IntygstjanstIntegrationConfig {

    private final ExchangeFilterFunctionProvider exchangeFilterFunctionProvider;

    @Bean(name = "intygstjanstWebClient")
    public WebClient webClientForIntygstjanst() {
        return WebClient.builder()
            .filter(exchangeFilterFunctionProvider.addHeadersFromMDCToRequest())
            .build();
    }
}
