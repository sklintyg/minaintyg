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
              return CertificateQuestionValueText.builder()
                  .value(integerValue.getValue().toString())
                  .build();
            }
        )
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
}
