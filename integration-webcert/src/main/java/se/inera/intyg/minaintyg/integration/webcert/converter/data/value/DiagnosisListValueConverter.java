package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DiagnosisListValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DIAGNOSIS_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var diagnosesTerminologies = ((CertificateDataConfigDiagnoses) element.getConfig()).getTerminology();
    final var certificateDataValueDiagnoses = ((CertificateDataValueDiagnosisList) element.getValue()).getList();

    final var headings = diagnosesTerminologies.stream()
        .map(DiagnosesTerminology::getLabel)
        .toList();

    final var values = certificateDataValueDiagnoses.stream()
        .map(diagnosis -> List.of(diagnosis.getCode(), diagnosis.getDescription()))
        .toList();

    return buildQuestionValueTable(headings, values);
  }

  private static CertificateQuestionValueTable buildQuestionValueTable(List<String> headings,
      List<List<String>> values) {
    return CertificateQuestionValueTable.builder()
        .headings(headings)
        .values(values)
        .build();
  }
}
