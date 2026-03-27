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
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class DateRangeValueConverterTest {

  private final ValueConverter dateRangeValueConverter = new DateRangeValueConverter();

  @Test
  void shallReturnDateValueType() {
    assertEquals(CertificateDataValueType.DATE_RANGE, dateRangeValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element =
        CertificateDataElement.builder()
            .value(CertificateDataValueDateRange.builder().build())
            .build();

    final var actualValue = dateRangeValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnNotProvidedValueIfFromIsNull() {
    final var element =
        CertificateDataElement.builder()
            .value(CertificateDataValueDateRange.builder().to(LocalDate.now()).build())
            .build();

    final var actualValue = dateRangeValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnNotProvidedValueIfToIsNull() {
    final var element =
        CertificateDataElement.builder()
            .value(CertificateDataValueDateRange.builder().from(LocalDate.now()).build())
            .build();

    final var actualValue = dateRangeValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnTextValueWithDateRangeIfDateRangeExists() {
    final var expectedValue =
        CertificateQuestionValueTable.builder()
            .headings(List.of("Från och med", "Till och med"))
            .values(List.of(List.of("2023-01-01", "2023-01-10")))
            .build();

    final var element =
        CertificateDataElement.builder()
            .value(
                CertificateDataValueDateRange.builder()
                    .from(LocalDate.parse("2023-01-01"))
                    .to(LocalDate.parse("2023-01-10"))
                    .build())
            .build();

    final var actualValue = dateRangeValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }
}
