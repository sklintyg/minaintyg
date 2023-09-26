package se.inera.intyg.minaintyg.certificate.legacy;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CertificateQuestionTable extends CertificateQuestion {

  //TODO: Can we make the format easier? Heading inside map maybe

  List<String> headings;
  Map<Integer, List<String>> values;

  @Builder.Default
  CertificateQuestionType type = CertificateQuestionType.TABLE;
}
