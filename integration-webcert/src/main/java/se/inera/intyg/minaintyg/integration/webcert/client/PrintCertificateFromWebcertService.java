package se.inera.intyg.minaintyg.integration.webcert.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.PrintCertificateRequestDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.PrintCertificateResponseDTO;

@Service
public class PrintCertificateFromWebcertService {

  private final WebClient webClient;
  private final String scheme;
  private final String baseUrl;
  private final int port;
  private final String endpoint;

  public PrintCertificateFromWebcertService(
      @Qualifier(value = "webcertWebClient") WebClient webClient,
      @Value("${integration.webcert.scheme}") String scheme,
      @Value("${integration.webcert.baseurl}") String baseUrl,
      @Value("${integration.webcert.port}") int port,
      @Value("${integration.webcert.printcertificate.endpoint}") String endpoint) {
    this.webClient = webClient;
    this.scheme = scheme;
    this.baseUrl = baseUrl;
    this.port = port;
    this.endpoint = endpoint;

  }

  public PrintCertificateResponseDTO print(PrintCertificateIntegrationRequest request) {
    return webClient.post().uri(uriBuilder -> uriBuilder
            .scheme(scheme)
            .host(baseUrl)
            .port(port)
            .path(endpoint)
            .build(request.getCertificateId())
        )
        .body(Mono.just(
            PrintCertificateRequestDTO
                .builder()
                .customizationId(request.getCustomizationId())
                .build()
        ), PrintCertificateRequestDTO.class)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(PrintCertificateResponseDTO.class)
        .share()
        .block();
  }
}
