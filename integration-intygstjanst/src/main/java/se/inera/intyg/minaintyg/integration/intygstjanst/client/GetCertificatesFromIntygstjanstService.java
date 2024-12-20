package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import static se.inera.intyg.minaintyg.integration.common.constants.ApplicationConstants.APPLICATION_INTYGSTJANST;
import static se.inera.intyg.minaintyg.logging.MdcLogConstants.EVENT_TYPE_INFO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException.GatewayTimeout;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationRequest;
import se.inera.intyg.minaintyg.integration.common.ExceptionThrowableFunction;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;
import se.inera.intyg.minaintyg.logging.PerformanceLogging;

@Service
public class GetCertificatesFromIntygstjanstService {

  private final WebClient webClient;
  private final String scheme;
  private final String baseUrl;
  private final int port;
  private final String endpoint;

  public GetCertificatesFromIntygstjanstService(
      @Qualifier(value = "intygstjanstWebClient") WebClient webClient,
      @Value("${integration.intygstjanst.scheme}") String scheme,
      @Value("${integration.intygstjanst.baseurl}") String baseUrl,
      @Value("${integration.intygstjanst.port}") int port,
      @Value("${integration.intygstjanst.certificate.endpoint}") String endpoint) {
    this.webClient = webClient;
    this.scheme = scheme;
    this.baseUrl = baseUrl;
    this.port = port;
    this.endpoint = endpoint;
  }

  @PerformanceLogging(eventAction = "retrieve-certificate-list-from-it", eventType = EVENT_TYPE_INFO)
  public CertificatesResponseDTO get(GetCertificateListIntegrationRequest request) {
    return webClient.post().uri(uriBuilder -> uriBuilder
            .scheme(scheme)
            .host(baseUrl)
            .port(port)
            .path(endpoint)
            .build())
        .body(Mono.just(request), GetCertificateListIntegrationRequest.class)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(CertificatesResponseDTO.class)
        .share()
        .onErrorMap(
            WebClientRequestException.class,
            ExceptionThrowableFunction.webClientRequest(APPLICATION_INTYGSTJANST)
        )
        .onErrorMap(
            GatewayTimeout.class,
            ExceptionThrowableFunction.gatewayTimeout(APPLICATION_INTYGSTJANST)
        )
        .block();
  }
}
