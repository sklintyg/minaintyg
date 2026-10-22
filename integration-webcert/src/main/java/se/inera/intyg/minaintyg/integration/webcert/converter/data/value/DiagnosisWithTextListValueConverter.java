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
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnosesWithText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisWithText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisWithTextList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

/**
 * Renders a list of diagnoses, each with a free text, as a three-column table: code, description,
 * text. Rows where neither the diagnosis code nor the text is filled in are left out; a question
 * with no such row renders as "Ej angivet".
 */
@Component
public class DiagnosisWithTextListValueConverter extends AbstractValueConverter {

  private static final String DIAGNOSIS = "Diagnos";
  private static final String DIAGNOSIS_CODE_WITH_TERMINOLOGY_LABEL = "Diagnoskod enligt %s";
  private static final String EMPTY = "";

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DIAGNOSIS_WITH_TEXT_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var answeredRows = getAnsweredRows(element.getValue());
    if (answeredRows.isEmpty()) {
      return NOT_PROVIDED_VALUE;
    }

    final var config = getConfig(element.getConfig());
    return CertificateQuestionValueTable.builder()
        .headings(getHeadings(answeredRows, config))
        .values(getValues(answeredRows))
        .build();
  }

  private static List<CertificateDataValueDiagnosisWithText> getAnsweredRows(
      CertificateDataValue value) {
    if (value instanceof CertificateDataValueDiagnosisWithTextList diagnosisWithTextList
        && diagnosisWithTextList.getList() != null) {
      return diagnosisWithTextList.getList().stream()
          .filter(DiagnosisWithTextListValueConverter::isAnswered)
          .toList();
    }
    return Collections.emptyList();
  }

  private static boolean isAnswered(CertificateDataValueDiagnosisWithText row) {
    return !isBlank(getCode(row)) || !isBlank(getText(row));
  }

  private static List<String> getHeadings(
      List<CertificateDataValueDiagnosisWithText> rows,
      Optional<CertificateDataConfigDiagnosesWithText> config) {
    final var terminologies =
        config
            .map(CertificateDataConfigDiagnosesWithText::getTerminology)
            .orElse(Collections.emptyList());
    return List.of(
        DIAGNOSIS_CODE_WITH_TERMINOLOGY_LABEL.formatted(getTerminologyLabel(rows, terminologies)),
        DIAGNOSIS,
        config.map(CertificateDataConfigDiagnosesWithText::getTextLabel).orElse(EMPTY));
  }

  private static String getTerminologyLabel(
      List<CertificateDataValueDiagnosisWithText> rows, List<DiagnosesTerminology> terminologies) {
    return rows.stream()
        .map(CertificateDataValueDiagnosisWithText::getDiagnosis)
        .filter(diagnosis -> diagnosis != null && diagnosis.getTerminology() != null)
        .findFirst()
        .map(CertificateDataValueDiagnosis::getTerminology)
        .map(
            terminologyId ->
                terminologies.stream()
                    .filter(terminology -> terminologyId.equalsIgnoreCase(terminology.getId()))
                    .findAny()
                    .map(DiagnosesTerminology::getLabel)
                    .orElse(terminologyId))
        .orElse(MISSING_LABEL);
  }

  private static List<List<String>> getValues(List<CertificateDataValueDiagnosisWithText> rows) {
    return rows.stream()
        .map(
            row ->
                List.of(orEmpty(getCode(row)), orEmpty(getDescription(row)), orEmpty(getText(row))))
        .toList();
  }

  private static String getCode(CertificateDataValueDiagnosisWithText row) {
    return Optional.ofNullable(row.getDiagnosis())
        .map(CertificateDataValueDiagnosis::getCode)
        .orElse(null);
  }

  private static String getDescription(CertificateDataValueDiagnosisWithText row) {
    return Optional.ofNullable(row.getDiagnosis())
        .map(CertificateDataValueDiagnosis::getDescription)
        .orElse(null);
  }

  private static String getText(CertificateDataValueDiagnosisWithText row) {
    return Optional.ofNullable(row.getText()).map(CertificateDataValueText::getText).orElse(null);
  }

  private static Optional<CertificateDataConfigDiagnosesWithText> getConfig(
      CertificateDataConfig config) {
    if (config instanceof CertificateDataConfigDiagnosesWithText diagnosesWithText) {
      return Optional.of(diagnosesWithText);
    }
    return Optional.empty();
  }

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  private static String orEmpty(String value) {
    return value == null ? EMPTY : value;
  }
}
