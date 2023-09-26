package se.inera.intyg.minaintyg.certificate.legacy;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class CertificateQuestion {

  String title;
  CertificateQuestionType type;
}
