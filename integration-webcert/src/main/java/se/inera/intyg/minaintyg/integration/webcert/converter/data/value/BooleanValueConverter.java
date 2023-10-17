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
      return value ? checkboxBoolean.getSelectedText() : checkboxBoolean.getUnselectedText();
    }
    if (config instanceof final CertificateDataConfigRadioBoolean radioBoolean) {
      return value ? radioBoolean.getSelectedText() : radioBoolean.getUnselectedText();
    }
    return TECHNICAL_ERROR;
  }

  private static CertificateQuestionValueText notProvidedTextValue() {
    return CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
  }
}
