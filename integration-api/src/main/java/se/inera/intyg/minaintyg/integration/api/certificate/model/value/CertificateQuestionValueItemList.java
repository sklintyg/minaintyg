package se.inera.intyg.minaintyg.integration.api.certificate.model.value;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateQuestionValueItemList implements CertificateQuestionValue {

  List<CertificationQuestionValueItem> values;

  @Override
  public CertificateQuestionValueType getType() {
    return CertificateQuestionValueType.ITEM_LIST;
  }
}
