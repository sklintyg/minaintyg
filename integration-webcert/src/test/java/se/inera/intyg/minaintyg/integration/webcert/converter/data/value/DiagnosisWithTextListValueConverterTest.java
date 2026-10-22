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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnosesWithText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisWithText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisWithTextList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class DiagnosisWithTextListValueConverterTest {

  private static final String TERMINOLOGY_ID = "ICD_10_SE";
  private static final String TERMINOLOGY_LABEL = "ICD-10-SE";
  private static final String UNKNOWN_TERMINOLOGY_ID = "UNKNOWN_TERMINOLOGY_ID";
  private static final String TEXT_LABEL = "När och var ställdes diagnosen?";
  private static final String CODE_ONE = "M545";
  private static final String DESCRIPTION_ONE = "Ländryggssmärta";
  private static final String TEXT_ONE = "2024, vårdcentralen Solna";
  private static final String CODE_TWO = "F32";
  private static final String DESCRIPTION_TWO = "Depressiv episod";
  private static final String TEXT_TWO = "2023, psykiatrin";
  private static final List<String> EXPECTED_HEADINGS =
      List.of("Diagnoskod enligt " + TERMINOLOGY_LABEL, "Diagnos", TEXT_LABEL);

  private final ValueConverter converter = new DiagnosisWithTextListValueConverter();

  @Test
  void shallReturnDiagnosisWithTextListValueType() {
    assertEquals(CertificateDataValueType.DIAGNOSIS_WITH_TEXT_LIST, converter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfListIsNull() {
    final var element =
        CertificateDataElement.builder()
            .config(createConfig())
            .value(CertificateDataValueDiagnosisWithTextList.builder().build())
            .build();

    assertEquals(NOT_PROVIDED_VALUE, converter.convert(element));
  }

  @Test
  void shallReturnNotProvidedValueIfNoRowIsAnswered() {
    final var element =
        createElement(
            emptyRow("diagnos1"), emptyRow("diagnos2"), row("diagnos3", null, null, null, "  "));

    assertEquals(NOT_PROVIDED_VALUE, converter.convert(element));
  }

  @Test
  void shallReturnAnsweredRowsInListOrderWithThreeColumns() {
    final var element =
        createElement(
            row("diagnos1", TERMINOLOGY_ID, CODE_ONE, DESCRIPTION_ONE, TEXT_ONE),
            row("diagnos2", TERMINOLOGY_ID, CODE_TWO, DESCRIPTION_TWO, TEXT_TWO),
            emptyRow("diagnos3"));

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals(
        List.of(
            List.of(CODE_ONE, DESCRIPTION_ONE, TEXT_ONE),
            List.of(CODE_TWO, DESCRIPTION_TWO, TEXT_TWO)),
        result.getValues());
  }

  @Test
  void shallReturnHeadingsFromTerminologyLabelAndTextLabel() {
    final var element =
        createElement(row("diagnos1", TERMINOLOGY_ID, CODE_ONE, DESCRIPTION_ONE, TEXT_ONE));

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals(EXPECTED_HEADINGS, result.getHeadings());
  }

  @Test
  void shallUseTerminologyIdInHeadingIfTerminologyIsNotInConfig() {
    final var element =
        createElement(row("diagnos1", UNKNOWN_TERMINOLOGY_ID, CODE_ONE, DESCRIPTION_ONE, TEXT_ONE));

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals("Diagnoskod enligt " + UNKNOWN_TERMINOLOGY_ID, result.getHeadings().getFirst());
  }

  @Test
  void shallUseTerminologyOfFirstAnsweredRowWithDiagnosisInHeading() {
    final var element =
        createElement(
            textOnlyRow("diagnos1", TEXT_ONE),
            row("diagnos2", TERMINOLOGY_ID, CODE_TWO, DESCRIPTION_TWO, TEXT_TWO));

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals("Diagnoskod enligt " + TERMINOLOGY_LABEL, result.getHeadings().getFirst());
  }

  @Test
  void shallUseEmptyTextHeadingIfConfigIsMissing() {
    final var element =
        CertificateDataElement.builder()
            .value(
                CertificateDataValueDiagnosisWithTextList.builder()
                    .list(
                        List.of(
                            row("diagnos1", TERMINOLOGY_ID, CODE_ONE, DESCRIPTION_ONE, TEXT_ONE)))
                    .build())
            .build();

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals("", result.getHeadings().getLast());
  }

  @Test
  void shallLeaveOutUnansweredRowBeforeAnsweredRow() {
    final var element =
        createElement(
            emptyRow("diagnos1"),
            row("diagnos2", TERMINOLOGY_ID, CODE_TWO, DESCRIPTION_TWO, TEXT_TWO));

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals(List.of(List.of(CODE_TWO, DESCRIPTION_TWO, TEXT_TWO)), result.getValues());
  }

  @Test
  void shallRenderEmptyDiagnosisColumnsForRowWithTextButNoDiagnosis() {
    final var element = createElement(textOnlyRow("diagnos1", TEXT_ONE));

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals(List.of(List.of("", "", TEXT_ONE)), result.getValues());
  }

  @Test
  void shallRenderEmptyTextColumnForRowWithDiagnosisButNoText() {
    final var element =
        createElement(
            CertificateDataValueDiagnosisWithText.builder()
                .id("diagnos1")
                .diagnosis(
                    CertificateDataValueDiagnosis.builder()
                        .terminology(TERMINOLOGY_ID)
                        .code(CODE_ONE)
                        .description(DESCRIPTION_ONE)
                        .build())
                .build());

    final var result = (CertificateQuestionValueTable) converter.convert(element);

    assertEquals(List.of(List.of(CODE_ONE, DESCRIPTION_ONE, "")), result.getValues());
  }

  private static CertificateDataElement createElement(
      CertificateDataValueDiagnosisWithText... rows) {
    return CertificateDataElement.builder()
        .config(createConfig())
        .value(CertificateDataValueDiagnosisWithTextList.builder().list(List.of(rows)).build())
        .build();
  }

  private static CertificateDataConfigDiagnosesWithText createConfig() {
    return CertificateDataConfigDiagnosesWithText.builder()
        .terminology(
            List.of(
                DiagnosesTerminology.builder().id(TERMINOLOGY_ID).label(TERMINOLOGY_LABEL).build()))
        .textLabel(TEXT_LABEL)
        .build();
  }

  private static CertificateDataValueDiagnosisWithText emptyRow(String id) {
    return row(id, TERMINOLOGY_ID, null, null, null);
  }

  private static CertificateDataValueDiagnosisWithText textOnlyRow(String id, String text) {
    return CertificateDataValueDiagnosisWithText.builder()
        .id(id)
        .text(CertificateDataValueText.builder().id(id + ".text").text(text).build())
        .build();
  }

  private static CertificateDataValueDiagnosisWithText row(
      String id, String terminology, String code, String description, String text) {
    return CertificateDataValueDiagnosisWithText.builder()
        .id(id)
        .diagnosis(
            CertificateDataValueDiagnosis.builder()
                .id(id + ".diagnos")
                .terminology(terminology)
                .code(code)
                .description(description)
                .build())
        .text(CertificateDataValueText.builder().id(id + ".text").text(text).build())
        .build();
  }
}
