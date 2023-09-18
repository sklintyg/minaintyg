package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;

@Service
public class GetPersonFromIntygProxyServiceImpl implements GetPersonFromIntygProxyService {

  private final WebClient webClient;
  private final String scheme;
  private final String baseUrl;
  private final int port;
  private final String puEndpoint;
  private final String traceIdHeader;
  private final String traceIdKey;

  public GetPersonFromIntygProxyServiceImpl(
      @Qualifier(value = "intygProxyWebClient") WebClient webClient,
      @Value("${integration.intygproxyservice.scheme}") String scheme,
      @Value("${integration.intygproxyservice.baseurl}") String baseUrl,
      @Value("${integration.intygproxyservice.port}") int port,
      @Value("${integration.intygproxyservice.person.endpoint}") String puEndpoint,
      @Value("${log.trace.header}") String traceIdHeader,
      @Value("${mdc.trace.id.key}") String traceIdKey) {
    this.webClient = webClient;
    this.scheme = scheme;
    this.baseUrl = baseUrl;
    this.port = port;
    this.puEndpoint = puEndpoint;
    this.traceIdHeader = traceIdHeader;
    this.traceIdKey = traceIdKey;
  }

  @Override
  public PersonSvarDTO getPersonFromIntygProxy(PersonRequest personRequest) {
    return webClient.post().uri(uriBuilder -> uriBuilder
            .scheme(scheme)
            .host(baseUrl)
            .port(port)
            .path(puEndpoint)
            .build())
        .body(Mono.just(personRequest), PersonRequest.class)
        .headers(httpHeaders -> {
          httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
          httpHeaders.set(traceIdHeader, MDC.get(traceIdKey));
        })
        .retrieve()
        .bodyToMono(PersonSvarDTO.class)
        .share()
        .block();
  }
}
