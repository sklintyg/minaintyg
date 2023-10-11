package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DateValueConverter extends AbstractValueConverter {

  @Override
  protected CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getDateValue(element.getValue())
        .map(
            dateValue -> {
              if (dateValue.getDate() == null) {
                return NOT_PROVIDED_VALUE;
              }
              return CertificateQuestionValueText.builder()
                  .value(dateValue.getDate().toString())
                  .build();
            }
        )
        .orElse(NOT_PROVIDED_VALUE);
  }

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DATE;
  }

  private Optional<CertificateDataValueDate> getDateValue(CertificateDataValue value) {
    if (value instanceof CertificateDataValueDate dateValue) {
      return Optional.of(dateValue);
    }
    return Optional.empty();
  }
}
