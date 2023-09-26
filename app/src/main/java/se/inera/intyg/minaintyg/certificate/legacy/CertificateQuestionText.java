package se.inera.intyg.minaintyg.certificate.legacy;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CertificateQuestionText extends CertificateQuestion {

  String value;
  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.TEXT;
}
