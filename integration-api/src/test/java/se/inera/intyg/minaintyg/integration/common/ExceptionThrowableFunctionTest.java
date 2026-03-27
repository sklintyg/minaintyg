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
package se.inera.intyg.minaintyg.integration.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class ExceptionThrowableFunctionTest {

  private static final String EXPECTED_APPLICATION_NAME = "applicationName";
  private static final String TEST = "test";
  private WebClientRequestException webClientRequestException;
  private WebClientResponseException webClientResponseException;

  @BeforeEach
  void setUp() {
    webClientRequestException =
        new WebClientRequestException(
            new RuntimeException(), HttpMethod.GET, URI.create(TEST), HttpHeaders.EMPTY);
    webClientResponseException = new WebClientResponseException(504, "", null, null, null);
  }

  @Nested
  class WebClientRequestExceptions {

    @Test
    void shouldReturnExceptionIncludingApplicationName() {
      final var function = ExceptionThrowableFunction.webClientRequest(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientRequestException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(EXPECTED_APPLICATION_NAME, result.getApplicationName());
    }

    @Test
    void shouldReturnExceptionIncludingMessage() {
      final var function = ExceptionThrowableFunction.webClientRequest(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientRequestException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(webClientRequestException.getMessage(), result.getMessage());
    }
  }

  @Nested
  class GatewayTimeout {

    @Test
    void shouldReturnExceptionIncludingApplicationName() {
      final var function = ExceptionThrowableFunction.gatewayTimeout(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientResponseException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(EXPECTED_APPLICATION_NAME, result.getApplicationName());
    }

    @Test
    void shouldReturnExceptionIncludingMessage() {
      final var function = ExceptionThrowableFunction.gatewayTimeout(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientResponseException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(webClientResponseException.getMessage(), result.getMessage());
    }
  }
}
