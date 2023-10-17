package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewText;

@Component
public class ViewTextValueConverter extends AbstractValueConverter {

  @Override
  protected CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getValue(element.getValue())
        .filter(value -> value.getText() != null)
        .map(
            value -> CertificateQuestionValueText.builder()
                .value(value.getText())
                .build()
        )
        .orElse(NOT_PROVIDED_VALUE);
  }

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.YEAR;
  }

  private Optional<CertificateDataValueViewText> getValue(CertificateDataValue value) {
    if (value instanceof CertificateDataValueViewText viewTextValue) {
      return Optional.of(viewTextValue);
    }
    return Optional.empty();
  }
}
