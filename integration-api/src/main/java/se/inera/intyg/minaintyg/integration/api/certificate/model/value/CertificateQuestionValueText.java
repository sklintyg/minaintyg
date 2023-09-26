package se.inera.intyg.minaintyg.integration.api.certificate.model.value;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateQuestionValueText implements CertificateQuestionValue {

  String value;

  @Override
  public CertificateQuestionValueType getType() {
    return CertificateQuestionValueType.TEXT;
  }
}
