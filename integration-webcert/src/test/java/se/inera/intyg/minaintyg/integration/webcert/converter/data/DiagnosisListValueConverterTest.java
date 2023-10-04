package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;

class DiagnosisListValueConverterTest {
  
  private static final String LABEL_VALUE = "labelValue";
  private static final String DIAGNOSIS_CODE_M79 = "M79";
  private static final String DIAGNOSIS_CODE_M79_DESCRIPTION = "M79 description";
  private static final String DIAGNOSIS_CODE_B36 = "B36";
  private static final String DIAGNOSIS_CODE_B36_DESCRIPTION = "B36 description";

  private final ValueConverter diagnosisListValueConverter = new DiagnosisListValueConverter();

  @Test
  void shouldConvertCertificateDataDiagnosisListValue() {
    final var config = CertificateDataConfigDiagnoses.builder()
        .terminology(
            List.of(
                DiagnosesTerminology.builder()
                    .label(LABEL_VALUE)
                    .build(),

                DiagnosesTerminology.builder()
                    .label(LABEL_VALUE)
                    .build()
            )
        )
        .build();

    final var value = CertificateDataValueDiagnosisList.builder()
        .list(
            List.of(
                CertificateDataValueDiagnosis.builder()
                    .code(DIAGNOSIS_CODE_B36)
                    .description(DIAGNOSIS_CODE_B36_DESCRIPTION)
                    .build(),
                CertificateDataValueDiagnosis.builder()
                    .code(DIAGNOSIS_CODE_M79)
                    .description(DIAGNOSIS_CODE_M79_DESCRIPTION)
                    .build()
            )
        )
        .build();

    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(
            List.of(
                LABEL_VALUE, LABEL_VALUE
            )
        )
        .values(
            List.of(
                List.of(
                    DIAGNOSIS_CODE_B36, DIAGNOSIS_CODE_B36_DESCRIPTION
                ),
                List.of(
                    DIAGNOSIS_CODE_M79, DIAGNOSIS_CODE_M79_DESCRIPTION
                )
            )
        )
        .build();

    final var elements = createElement(config, value);
    final var result = diagnosisListValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}