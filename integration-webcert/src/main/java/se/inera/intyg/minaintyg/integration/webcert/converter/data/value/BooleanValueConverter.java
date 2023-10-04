package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class BooleanValueConverter extends AbstractValueConverter {

  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";

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
        .value(value ? TRUE_BOOLEAN : FALSE_BOOLEAN)
        .build();
  }

  private static CertificateQuestionValueText notProvidedTextValue() {
    return CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
  }
}
