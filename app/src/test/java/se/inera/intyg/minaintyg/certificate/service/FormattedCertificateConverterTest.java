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
package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;

@ExtendWith(MockitoExtension.class)
class FormattedCertificateConverterTest {

  private static final Certificate certificate =
      Certificate.builder()
          .metadata(CertificateMetadata.builder().build())
          .categories(List.of(CertificateCategory.builder().build()))
          .build();

  private static final FormattedCertificateCategory category =
      FormattedCertificateCategory.builder().heading("Title").body("Body").build();
  @Mock FormattedCategoryConverter formattedCategoryConverter;
  @InjectMocks FormattedCertificateConverter formattedCertificateConverter;

  @BeforeEach
  void setup() {
    when(formattedCategoryConverter.convert(any(CertificateCategory.class))).thenReturn(category);
  }

  @Test
  void shouldConvertContent() {
    final var result = formattedCertificateConverter.convert(certificate);

    assertEquals(1, result.getContent().size());
    assertEquals(category, result.getContent().get(0));
  }

  @Test
  void shouldConvertMetadata() {
    final var result = formattedCertificateConverter.convert(certificate);

    assertEquals(certificate.getMetadata(), result.getMetadata());
  }

  @Test
  void shouldCallCategoryConverter() {
    final var captor = ArgumentCaptor.forClass(CertificateCategory.class);

    formattedCertificateConverter.convert(certificate);

    verify(formattedCategoryConverter).convert(captor.capture());
    assertEquals(certificate.getCategories().get(0), captor.getValue());
  }
}
