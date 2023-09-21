package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import lombok.Data;

@Data
public abstract class CertificateQuestion {

  String title;
  CertificateQuestionType type;
}
