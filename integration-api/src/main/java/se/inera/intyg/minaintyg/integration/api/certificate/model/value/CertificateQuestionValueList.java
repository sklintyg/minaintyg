package se.inera.intyg.minaintyg.integration.api.certificate.model.value;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateQuestionValueList implements CertificateQuestionValue {

  List<String> values;

  @Override
  public CertificateQuestionValueType getType() {
    return CertificateQuestionValueType.LIST;
  }
}
