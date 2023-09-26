package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CertificateQuestionListValue extends CertificateQuestionValue {

  List<String> values;
  @Builder.Default
  CertificateQuestionValueType type = CertificateQuestionValueType.LIST;
}
