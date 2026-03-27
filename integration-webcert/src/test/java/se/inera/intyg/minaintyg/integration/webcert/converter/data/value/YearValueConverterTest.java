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
package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueYear;

class YearValueConverterTest {

  private final ValueConverter yearValueConverter = new YearValueConverter();

  @Test
  void shallReturnYearValueType() {
    assertEquals(CertificateDataValueType.YEAR, yearValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element =
        CertificateDataElement.builder().value(CertificateDataValueYear.builder().build()).build();

    final var actualValue = yearValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnTextValueWithYearIfYearExists() {
    final var expectedValue = CertificateQuestionValueText.builder().value("2023").build();

    final var element =
        CertificateDataElement.builder()
            .value(
                CertificateDataValueYear.builder()
                    .year(Integer.valueOf(expectedValue.getValue()))
                    .build())
            .build();

    final var actualValue = yearValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallThrowIllegalArgumentIfWrongType() {
    final var element =
        CertificateDataElement.builder().value(CertificateDataValueText.builder().build()).build();

    assertThrows(IllegalArgumentException.class, () -> yearValueConverter.convert(element));
  }
}
