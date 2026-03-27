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
package se.inera.intyg.minaintyg.integrationtest.util;

import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import se.inera.intyg.minaintyg.auth.FakeCredentials;
import se.inera.intyg.minaintyg.information.dto.InformationResponseDTO;
import se.inera.intyg.minaintyg.testability.TestPersonResponse;

@RequiredArgsConstructor
public class ApiUtil {

  private final TestRestTemplate restTemplate;
  private final int port;
  private String xsrfToken;
  private String session;

  public ResponseEntity<InformationResponseDTO> information() {
    final var requestUrl = "http://localhost:" + port + "/api/info";
    final var headers = new HttpHeaders();
    headers.add("COOKIE", "SESSION=%s; XSRF-TOKEN=%s;".formatted(session, xsrfToken));
    headers.add("X-Xsrf-Token", xsrfToken);
    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<TestPersonResponse> testabilityGetPersons() {
    final var requestUrl = "http://localhost:" + port + "/api/testability/person";
    final var headers = new HttpHeaders();
    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<Void> testabilityFakeLogin(String personId) {
    final var requestUrl = "http://localhost:" + port + "/api/testability/fake";
    final var headers = new HttpHeaders();
    final ResponseEntity<Void> response =
        this.restTemplate.exchange(
            requestUrl,
            HttpMethod.POST,
            new HttpEntity<>(FakeCredentials.builder().personId(personId).build(), headers),
            new ParameterizedTypeReference<>() {},
            Collections.emptyMap());

    xsrfToken = getHeader(response, "XSRF-TOKEN");
    session = getHeader(response, "SESSION");

    return response;
  }

  public ResponseEntity<Void> testabilityFakeLogout() {
    final var requestUrl = "http://localhost:" + port + "/api/testability/logout";
    final var headers = new HttpHeaders();
    headers.add("COOKIE", "SESSION=%s; XSRF-TOKEN=%s;".formatted(session, xsrfToken));
    headers.add("X-Xsrf-Token", xsrfToken);
    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  @NotNull private static String getHeader(ResponseEntity<Void> response, String s) {
    return Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).stream()
        .filter(str -> str.contains(s))
        .findAny()
        .map(str -> str.substring(str.indexOf("=") + 1, str.indexOf(";")))
        .orElse("missing");
  }
}
