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
package se.inera.intyg.minaintyg.integrationtest.environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@RequiredArgsConstructor
public class IntygsadminMock {

  private final MockServerClient mockServerClient;

  public void emptyBanners() {
    try {
      mockServerClient
          .when(HttpRequest.request("/actuator/banner/MINA_INTYG"))
          .respond(
              HttpResponse.response(
                      JsonMapper.builder()
                          .addModule(new JavaTimeModule())
                          .build()
                          .writeValueAsString(Collections.emptyList()))
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON));
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException(ex);
    }
  }

  public void foundBanners(BannerDTO... bannerDTOS) {
    try {
      mockServerClient
          .when(HttpRequest.request("/actuator/banner/MINA_INTYG"))
          .respond(
              HttpResponse.response(
                      JsonMapper.builder()
                          .addModule(new JavaTimeModule())
                          .build()
                          .writeValueAsString(bannerDTOS))
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON));
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
