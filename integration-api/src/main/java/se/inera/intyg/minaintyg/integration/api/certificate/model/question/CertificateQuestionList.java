package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CertificateQuestionList extends CertificateQuestion {

  List<String> values;
  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.LIST;
}
