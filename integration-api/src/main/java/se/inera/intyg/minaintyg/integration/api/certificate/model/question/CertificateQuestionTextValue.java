package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CertificateQuestionTextValue extends CertificateQuestionValue {

  String value;
  @Builder.Default
  CertificateQuestionValueType type = CertificateQuestionValueType.TEXT;
}
