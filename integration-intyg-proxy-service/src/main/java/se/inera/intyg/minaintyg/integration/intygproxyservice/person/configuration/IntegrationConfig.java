package se.inera.intyg.minaintyg.integration.intygproxyservice.person.configuration;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class IntegrationConfig {

  @Value("${log.trace.header}")
  private String traceIdHeader;
  @Value("${mdc.trace.id.key}")
  private String traceIdKey;

  @Bean(name = "intygProxyWebClient")
  public WebClient webClientForIntygProxy() {
    return WebClient.builder()
        .filter(ExchangeFilterFunction.ofRequestProcessor(
            request -> Mono.just(ClientRequest.from(request)
                .header(traceIdHeader, MDC.get(traceIdKey))
                .build())
        ))
        .build();
  }
}
