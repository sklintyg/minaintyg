package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CertificateQuestionTextList extends CertificateQuestion {

  Map<String, String> values;
  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.LIST;
}
