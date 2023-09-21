package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CertificateQuestionText extends CertificateQuestion {

  String value;
  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.TEXT;
}
