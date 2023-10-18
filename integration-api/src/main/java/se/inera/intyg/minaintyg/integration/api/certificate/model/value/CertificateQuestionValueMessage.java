package se.inera.intyg.minaintyg.integration.api.certificate.model.value;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.MessageLevel;

@Value
@Builder
public class CertificateQuestionValueMessage implements CertificateQuestionValue {

  String value;
  MessageLevel level;

  @Override
  public CertificateQuestionValueType getType() {
    return CertificateQuestionValueType.MESSAGE;
  }
}
