package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;

@Value
@Builder
public class CertificateQuestion {

  String title;
  String label;
  CertificateQuestionValue value;
  List<CertificateQuestion> subQuestions;
}
