package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CertificateQuestionTable extends CertificateQuestion {

  //TODO: Can we make the format easier? Heading inside map maybe

  List<String> headings;
  Map<Integer, List<String>> values;

  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.TABLE;
}
