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
package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.X_FRAME_OPTIONS;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class CustomXFrameOptionsHeaderWriterTest {

  private static final String POST = "POST";
  private static final String REQUEST_URI_PDF = "/pdf";
  private static final String NOT_REQUEST_URI_PDF = "/other";
  private HttpServletRequest request;
  private HttpServletResponse response;
  private CustomXFrameOptionsHeaderWriter customXFrameOptionsHeaderWriter;

  @BeforeEach
  void setUp() {
    customXFrameOptionsHeaderWriter = new CustomXFrameOptionsHeaderWriter();
    response = new MockHttpServletResponse();
  }

  @Test
  void shouldSetFrameOptionsToSameOrigin() {
    request = new MockHttpServletRequest(POST, REQUEST_URI_PDF);
    customXFrameOptionsHeaderWriter.writeHeaders(request, response);
    assertEquals(List.of("SAMEORIGIN"), response.getHeaders(X_FRAME_OPTIONS));
  }

  @Test
  void shouldSetFrameOptionsToDeny() {
    request = new MockHttpServletRequest(POST, NOT_REQUEST_URI_PDF);
    customXFrameOptionsHeaderWriter.writeHeaders(request, response);
    assertEquals(List.of("DENY"), response.getHeaders(X_FRAME_OPTIONS));
  }
}
