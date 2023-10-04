package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class CodeValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.CODE;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    if (!(element.getConfig() instanceof final CertificateDataConfigRadioMultipleCodeOptionalDropdown dataConfig)) {
      return notProvidedTextValue();
    }
    final var dataValue = (CertificateDataValueCode) element.getValue();
    final var radioMultipleCodeOptionalDropdownLabel = dataConfig.getList().stream()
        .filter(config -> config.getId().equals(dataValue.getCode()))
        .map(RadioMultipleCodeOptionalDropdown::getLabel)
        .toList();

    return CertificateQuestionValueText.builder()
        .value(radioMultipleCodeOptionalDropdownLabel.isEmpty() ? NOT_PROVIDED
            : radioMultipleCodeOptionalDropdownLabel.get(0))
        .build();
  }

  private static CertificateQuestionValueText notProvidedTextValue() {
    return CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
  }
}
