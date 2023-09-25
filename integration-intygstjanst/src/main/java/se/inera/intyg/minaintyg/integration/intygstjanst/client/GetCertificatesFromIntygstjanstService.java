package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateListRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

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

  public CertificatesResponseDTO get(CertificateListRequest request) {
    return webClient.post().uri(uriBuilder -> uriBuilder
            .scheme(scheme)
            .host(baseUrl)
            .port(port)
            .path(endpoint)
            .build())
        .body(Mono.just(request), CertificateListRequest.class)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(CertificatesResponseDTO.class)
        .share()
        .block();
  }
}
