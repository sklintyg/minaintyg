package se.inera.intyg.minaintyg.integration.intygsadmin.client;

import static se.inera.intyg.minaintyg.integration.common.constants.ApplicationConstants.APPLICATION_INTYGSADMIN;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException.GatewayTimeout;
import se.inera.intyg.minaintyg.integration.common.ExceptionThrowableFunction;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@Service
public class GetBannersFromIntygsadminService {

  private final WebClient webClient;
  private final String scheme;
  private final String baseUrl;
  private final int port;
  private final String endpoint;

  public GetBannersFromIntygsadminService(
      @Qualifier(value = "intygsadminWebClient") WebClient webClient,
      @Value("${integration.intygsadmin.scheme}") String scheme,
      @Value("${integration.intygsadmin.baseurl}") String baseUrl,
      @Value("${integration.intygsadmin.port}") int port,
      @Value("${integration.intygsadmin.banner.endpoint}") String endpoint) {
    this.webClient = webClient;
    this.scheme = scheme;
    this.baseUrl = baseUrl;
    this.port = port;
    this.endpoint = endpoint;
  }

  public List<BannerDTO> get() {
    final var banners = webClient.get().uri(uriBuilder -> uriBuilder
            .scheme(scheme)
            .host(baseUrl)
            .port(port)
            .path(endpoint)
            .build(ApplicationDTO.MINA_INTYG))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(BannerDTO[].class)
        .share()
        .onErrorMap(
            WebClientRequestException.class,
            ExceptionThrowableFunction.webClientRequest(APPLICATION_INTYGSADMIN)
        )
        .onErrorMap(
            GatewayTimeout.class,
            ExceptionThrowableFunction.gatewayTimeout(APPLICATION_INTYGSADMIN)
        )
        .block();

    return banners != null ? Arrays.stream(banners).toList() : Collections.emptyList();
  }
}
