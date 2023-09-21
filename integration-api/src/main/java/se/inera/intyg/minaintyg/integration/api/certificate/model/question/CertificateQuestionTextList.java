package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CertificateQuestionTextList extends CertificateQuestion {

  Map<String, String> values;
  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.TEXT_LIST;
}
