package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestion;

@Value
@Builder
public class CertificateCategory {

  String title;
  List<CertificateQuestion> questions;
}
