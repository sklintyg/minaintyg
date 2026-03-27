/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.integration.webcert.client;

import static se.inera.intyg.minaintyg.integration.common.constants.ApplicationConstants.APPLICATION_WEBCERT;
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
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.common.ExceptionThrowableFunction;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.logging.PerformanceLogging;

@Service
public class GetCertificateFromWebcertService {

  private final WebClient webClient;
  private final String scheme;
  private final String baseUrl;
  private final int port;
  private final String endpoint;

  public GetCertificateFromWebcertService(
      @Qualifier(value = "webcertWebClient") WebClient webClient,
      @Value("${integration.webcert.scheme}") String scheme,
      @Value("${integration.webcert.baseurl}") String baseUrl,
      @Value("${integration.webcert.port}") int port,
      @Value("${integration.webcert.certificate.endpoint}") String endpoint) {
    this.webClient = webClient;
    this.scheme = scheme;
    this.baseUrl = baseUrl;
    this.port = port;
    this.endpoint = endpoint;
  }

  @PerformanceLogging(eventAction = "retrieve-certificate-from-wc", eventType = EVENT_TYPE_INFO)
  public CertificateResponseDTO get(GetCertificateIntegrationRequest request) {
    return webClient
        .post()
        .uri(
            uriBuilder ->
                uriBuilder
                    .scheme(scheme)
                    .host(baseUrl)
                    .port(port)
                    .path(endpoint)
                    .build(request.getCertificateId()))
        .body(Mono.just(request), GetCertificateIntegrationRequest.class)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(CertificateResponseDTO.class)
        .share()
        .onErrorMap(
            WebClientRequestException.class,
            ExceptionThrowableFunction.webClientRequest(APPLICATION_WEBCERT))
        .onErrorMap(
            GatewayTimeout.class, ExceptionThrowableFunction.gatewayTimeout(APPLICATION_WEBCERT))
        .block();
  }
}
