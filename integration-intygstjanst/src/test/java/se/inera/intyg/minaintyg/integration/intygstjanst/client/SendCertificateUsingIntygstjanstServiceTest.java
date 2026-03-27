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
package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;

@ExtendWith(MockitoExtension.class)
class SendCertificateUsingIntygstjanstServiceTest {

  private static final String CERTIFICATE_ID = "certificateId";
  private static MockWebServer mockWebServer;
  private SendCertificateUsingIntygstjanstService sendCertificateUsingIntygstjanstService;

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
    final var endpoint = "/inera-certificate/internalapi/citizen/send";
    sendCertificateUsingIntygstjanstService =
        new SendCertificateUsingIntygstjanstService(
            WebClient.create(baseUrl), scheme, baseUrl, mockWebServer.getPort(), endpoint);
  }

  @Test
  void shouldNotThrowError() {
    final var request =
        SendCertificateIntegrationRequest.builder().certificateId(CERTIFICATE_ID).build();

    mockWebServer.enqueue(new MockResponse().addHeader("Content-Type", "application/json"));

    assertDoesNotThrow(() -> sendCertificateUsingIntygstjanstService.send(request));
  }
}
