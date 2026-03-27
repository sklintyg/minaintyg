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

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class BooleanValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.BOOLEAN;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = ((CertificateDataValueBoolean) element.getValue()).getSelected();
    if (value == null) {
      return notProvidedTextValue();
    }
    return CertificateQuestionValueText.builder()
        .value(getDisplayValueFromConfig(element.getConfig(), value))
        .build();
  }

  private String getDisplayValueFromConfig(CertificateDataConfig config, Boolean value) {
    if (config instanceof final CertificateDataConfigCheckboxBoolean checkboxBoolean) {
      return Boolean.TRUE.equals(value)
          ? checkboxBoolean.getSelectedText()
          : checkboxBoolean.getUnselectedText();
    }
    if (config instanceof final CertificateDataConfigRadioBoolean radioBoolean) {
      return Boolean.TRUE.equals(value)
          ? radioBoolean.getSelectedText()
          : radioBoolean.getUnselectedText();
    }
    return value.toString();
  }

  private static CertificateQuestionValueText notProvidedTextValue() {
    return CertificateQuestionValueText.builder().value(NOT_PROVIDED).build();
  }
}
