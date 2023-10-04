package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;

public abstract class AbstractValueConverter implements ValueConverter {

  @Override
  public CertificateQuestionValue convert(CertificateDataElement element) {
    if (!getType().equals(element.getValue().getType())) {
      throw new IllegalArgumentException(
          "Wrong type! '%s'".formatted(element.getValue().getType())
      );
    }
    return convertToValue(element);
  }

  protected abstract CertificateQuestionValue convertToValue(CertificateDataElement element);
}
