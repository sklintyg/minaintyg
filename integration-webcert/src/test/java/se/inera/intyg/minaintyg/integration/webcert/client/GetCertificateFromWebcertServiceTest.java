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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class GetCertificateFromWebcertServiceTest {

  private static MockWebServer mockWebServer;
  private GetCertificateFromWebcertService getCertificateFromWebcertService;

  private static final String CERTIFICATE_ID = "certificateId";
  private static final String ID = "id";
  private final ObjectMapper objectMapper = JsonMapper.builder().build();

  @BeforeAll
  static void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @BeforeEach
  void initialize() {
    final var scheme = "http";
    final var baseUrl = "localhost";
    final var getPersonEndpoint = "/internalapi/certificate/{certificateId}";
    getCertificateFromWebcertService =
        new GetCertificateFromWebcertService(
            WebClient.create(baseUrl), scheme, baseUrl, mockWebServer.getPort(), getPersonEndpoint);
  }

  @Test
  void shouldReturnGetCertificateResponse() {
    final var getCertificateRequest =
        GetCertificateIntegrationRequest.builder().certificateId(CERTIFICATE_ID).build();
    final var expectedResponse =
        CertificateResponseDTO.builder()
            .certificate(
                CertificateDTO.builder()
                    .data(Map.of(ID, CertificateDataElement.builder().build()))
                    .metadata(CertificateMetadataDTO.builder().build())
                    .build())
            .build();
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));
    final var actualResponse = getCertificateFromWebcertService.get(getCertificateRequest);
    assertEquals(expectedResponse, actualResponse);
  }
}
