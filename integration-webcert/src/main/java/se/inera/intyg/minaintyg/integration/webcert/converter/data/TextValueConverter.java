package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class TextValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.TEXT;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = ((CertificateDataTextValue) element.getValue()).getText();
    return getText(value);
  }

  private static CertificateQuestionValueText getText(String value) {
    return CertificateQuestionValueText.builder()
        .value(value != null ? value : NOT_PROVIDED)
        .build();
  }
}
