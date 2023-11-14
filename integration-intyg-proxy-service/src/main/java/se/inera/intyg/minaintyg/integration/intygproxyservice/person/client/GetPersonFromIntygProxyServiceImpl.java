package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import static se.inera.intyg.minaintyg.integration.common.constants.ApplicationConstants.APPLICATION_INTYG_PROXY_SERVICE;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException.GatewayTimeout;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.common.ExceptionThrowableFunction;

@Service
public class GetPersonFromIntygProxyServiceImpl implements GetPersonFromIntygProxyService {

  private final WebClient webClient;
  private final String scheme;
  private final String baseUrl;
  private final int port;
  private final String puEndpoint;

  public GetPersonFromIntygProxyServiceImpl(
      @Qualifier(value = "intygProxyWebClient") WebClient webClient,
      @Value("${integration.intygproxyservice.scheme}") String scheme,
      @Value("${integration.intygproxyservice.baseurl}") String baseUrl,
      @Value("${integration.intygproxyservice.port}") int port,
      @Value("${integration.intygproxyservice.person.endpoint}") String puEndpoint) {
    this.webClient = webClient;
    this.scheme = scheme;
    this.baseUrl = baseUrl;
    this.port = port;
    this.puEndpoint = puEndpoint;
  }

  @Override
  public PersonSvarDTO getPersonFromIntygProxy(GetPersonIntegrationRequest personRequest) {
    return webClient.post().uri(uriBuilder -> uriBuilder
            .scheme(scheme)
            .host(baseUrl)
            .port(port)
            .path(puEndpoint)
            .build())
        .body(Mono.just(personRequest), GetPersonIntegrationRequest.class)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(PersonSvarDTO.class)
        .share()
        .onErrorMap(
            WebClientRequestException.class,
            ExceptionThrowableFunction.webClientRequest(APPLICATION_INTYG_PROXY_SERVICE)
        )
        .onErrorMap(
            GatewayTimeout.class,
            ExceptionThrowableFunction.gatewayTimeout(APPLICATION_INTYG_PROXY_SERVICE)
        )
        .block();
  }
}
