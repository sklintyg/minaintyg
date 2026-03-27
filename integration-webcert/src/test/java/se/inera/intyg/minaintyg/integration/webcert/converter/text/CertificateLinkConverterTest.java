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
package se.inera.intyg.minaintyg.integration.webcert.converter.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateLinkDTO;

@ExtendWith(MockitoExtension.class)
class CertificateLinkConverterTest {

  private static final CertificateLinkDTO link =
      CertificateLinkDTO.builder().url("URL").id("ID").name("NAME").build();

  @InjectMocks CertificateLinkConverter certificateLinkConverter;

  @Test
  void shouldConvertId() {
    final var response = certificateLinkConverter.convert(link);

    assertEquals(link.getId(), response.getId());
  }

  @Test
  void shouldConvertName() {
    final var response = certificateLinkConverter.convert(link);

    assertEquals(link.getName(), response.getName());
  }

  @Test
  void shouldConvertUrl() {
    final var response = certificateLinkConverter.convert(link);

    assertEquals(link.getUrl(), response.getUrl());
  }
}
