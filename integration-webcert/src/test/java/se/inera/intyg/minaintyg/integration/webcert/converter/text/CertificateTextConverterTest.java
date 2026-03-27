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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateLink;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTextType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateLinkDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateTextDTO;

@ExtendWith(MockitoExtension.class)
class CertificateTextConverterTest {

  private static final CertificateLinkDTO linkDTO =
      CertificateLinkDTO.builder().url("URL").id("ID").name("NAME").build();

  private static final CertificateLink link =
      CertificateLink.builder().url("URL").id("ID").name("NAME").build();

  private static final CertificateTextDTO certificateText =
      CertificateTextDTO.builder()
          .text("TEXT")
          .type(CertificateTextType.PREAMBLE_TEXT)
          .links(List.of(linkDTO, linkDTO))
          .build();

  private static final CertificateTextDTO certificateTextNoLinks =
      CertificateTextDTO.builder().text("TEXT").type(CertificateTextType.PREAMBLE_TEXT).build();

  @Mock CertificateLinkConverter certificateLinkConverter;

  @InjectMocks CertificateTextConverter certificateTextConverter;

  @Nested
  class HasLinks {

    @BeforeEach
    void setup() {
      when(certificateLinkConverter.convert(any(CertificateLinkDTO.class))).thenReturn(link);
    }

    @Test
    void shouldConvertText() {
      final var response = certificateTextConverter.convert(certificateText);

      assertEquals(certificateText.getText(), response.getText());
    }

    @Test
    void shouldConvertType() {
      final var response = certificateTextConverter.convert(certificateText);

      assertEquals(certificateText.getType(), response.getType());
    }

    @Test
    void shouldConvertLink() {
      final var response = certificateTextConverter.convert(certificateText);

      assertEquals(link, response.getLinks().get(0));
    }

    @Test
    void shouldConvertLinks() {
      final var response = certificateTextConverter.convert(certificateText);

      assertEquals(2, response.getLinks().size());
    }
  }

  @Test
  void shouldConvertNullLinks() {
    final var response = certificateTextConverter.convert(certificateTextNoLinks);

    assertEquals(Collections.emptyList(), response.getLinks());
  }
}
