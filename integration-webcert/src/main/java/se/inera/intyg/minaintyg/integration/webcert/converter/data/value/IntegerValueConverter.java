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

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueInteger;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class IntegerValueConverter extends AbstractValueConverter {

  @Override
  protected CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getIntegerValue(element.getValue())
        .map(
            integerValue -> {
              if (integerValue.getValue() == null) {
                return NOT_PROVIDED_VALUE;
              }
              return CertificateQuestionValueText.builder().value(getValue(integerValue)).build();
            })
        .orElse(NOT_PROVIDED_VALUE);
  }

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.INTEGER;
  }

  private Optional<CertificateDataValueInteger> getIntegerValue(CertificateDataValue value) {
    if (value instanceof CertificateDataValueInteger integerValue) {
      return Optional.of(integerValue);
    }
    return Optional.empty();
  }

  private String getValue(CertificateDataValueInteger integerValue) {
    if (integerValue.getUnitOfMeasurement() != null) {
      return integerValue.getValue() + integerValue.getUnitOfMeasurement();
    }
    return integerValue.getValue().toString();
  }
}
