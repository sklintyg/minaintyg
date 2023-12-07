package se.inera.intyg.minaintyg.integration.api.certificate.model.value;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateQuestionValueGeneralTable implements CertificateQuestionValue {

  List<TableElement> headings;
  List<List<TableElement>> values;

  @Override
  public CertificateQuestionValueType getType() {
    return CertificateQuestionValueType.GENERAL_TABLE;
  }
}
