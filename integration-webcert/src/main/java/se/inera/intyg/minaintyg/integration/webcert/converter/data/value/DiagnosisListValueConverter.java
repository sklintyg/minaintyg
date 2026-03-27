/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DiagnosisListValueConverter extends AbstractValueConverter {

  public static final String DIAGNOSIS = "Diagnos";
  public static final String MISSING = "Saknas";
  private static final String DIAGNOSIS_CODE_WITH_TERMINLOGOY_LABEL = "Diagnoskod enligt %s";

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DIAGNOSIS_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var terminologies = getTerminologies(element.getConfig());
    final var diagnoses = getDiagnoses(element.getValue());
    if (diagnoses.isEmpty() || diagnoses.get().isEmpty()) {
      return NOT_PROVIDED_VALUE;
    }

    return CertificateQuestionValueTable.builder()
        .headings(getHeadings(diagnoses.get(), terminologies.orElse(Collections.emptyList())))
        .values(getValues(diagnoses.get()))
        .build();
  }

  private static List<String> getHeadings(
      List<CertificateDataValueDiagnosis> diagnoses, List<DiagnosesTerminology> terminologies) {
    return List.of(
        String.format(
            DIAGNOSIS_CODE_WITH_TERMINLOGOY_LABEL, getTerminologyLabel(diagnoses, terminologies)),
        DIAGNOSIS);
  }

  private static String getTerminologyLabel(
      List<CertificateDataValueDiagnosis> diagnoses, List<DiagnosesTerminology> terminologies) {
    return diagnoses.stream()
        .findFirst()
        .map(CertificateDataValueDiagnosis::getTerminology)
        .map(
            terminologyId ->
                terminologies.stream()
                    .filter(terminology -> terminology.getId().equalsIgnoreCase(terminologyId))
                    .findAny()
                    .map(DiagnosesTerminology::getLabel)
                    .orElse(terminologyId))
        .orElse(MISSING);
  }

  private static List<List<String>> getValues(List<CertificateDataValueDiagnosis> diagnoses) {
    return diagnoses.stream()
        .map(diagnosis -> List.of(diagnosis.getCode(), diagnosis.getDescription()))
        .toList();
  }

  private Optional<List<DiagnosesTerminology>> getTerminologies(CertificateDataConfig config) {
    if (config instanceof CertificateDataConfigDiagnoses configDiagnoses) {
      return Optional.ofNullable(configDiagnoses.getTerminology());
    }
    return Optional.empty();
  }

  private Optional<List<CertificateDataValueDiagnosis>> getDiagnoses(CertificateDataValue value) {
    if (value instanceof CertificateDataValueDiagnosisList diagnosisList) {
      return Optional.ofNullable(diagnosisList.getList());
    }
    return Optional.empty();
  }
}
