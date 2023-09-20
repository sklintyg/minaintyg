package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CertificateQuestionTable extends CertificateQuestion {

  List<String> headings;
  Map<Integer, String> values;

  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.TABLE;
}
