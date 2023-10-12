package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDouble;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DoubleValueConverter extends AbstractValueConverter {

  @Override
  protected CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getDoubleValue(element.getValue())
        .map(
            doubleValue -> {
              if (doubleValue.getValue() == null) {
                return NOT_PROVIDED_VALUE;
              }
              return CertificateQuestionValueText.builder()
                  .value(doubleValue.getValue().toString().replace(".", ","))
                  .build();
            }
        )
        .orElse(NOT_PROVIDED_VALUE);
  }

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DOUBLE;
  }

  private Optional<CertificateDataValueDouble> getDoubleValue(CertificateDataValue value) {
    if (value instanceof CertificateDataValueDouble doubleValue) {
      return Optional.of(doubleValue);
    }
    return Optional.empty();
  }
}
