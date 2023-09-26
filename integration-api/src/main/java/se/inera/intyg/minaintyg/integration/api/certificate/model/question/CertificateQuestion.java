package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateQuestion {

  String title;
  CertificateQuestionValue value;
  @Builder.Default
  List<CertificateSubQuestion> subQuestions = Collections.emptyList();
}
