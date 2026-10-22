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
package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnosesWithText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesWithTextListItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisWithText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisWithTextList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class DiagnosisWithTextListJsonTest {

  private static final String CONFIG_JSON =
      """
      {
        "type": "UE_DIAGNOSES_WITH_TEXT",
        "header": null, "icon": null, "accordion": null, "message": null,
        "text": "Diagnos eller diagnoser för de besvär som patienten anser är en arbetsskada",
        "description": "Använd den mest specifika diagnosen som beskriver besvären baserat på de utredningar som gjorts.",
        "label": null,
        "terminology": [ { "id": "ICD_10_SE", "label": "ICD-10-SE" } ],
        "textLabel": "När och var ställdes diagnosen?",
        "textLimit": 50,
        "list": [
          { "id": "diagnos1", "diagnosisId": "diagnos1.diagnos", "textId": "diagnos1.text" },
          { "id": "diagnos2", "diagnosisId": "diagnos2.diagnos", "textId": "diagnos2.text" },
          { "id": "diagnos3", "diagnosisId": "diagnos3.diagnos", "textId": "diagnos3.text" }
        ]
      }
      """;

  private static final String VALUE_JSON =
      """
      {
        "type": "DIAGNOSIS_WITH_TEXT_LIST",
        "list": [
          { "type": "DIAGNOSIS_WITH_TEXT", "id": "diagnos1",
            "diagnosis": { "type": "DIAGNOSIS", "id": "diagnos1.diagnos", "terminology": "ICD_10_SE", "code": "M545", "description": "Ländryggssmärta" },
            "text":      { "type": "TEXT",      "id": "diagnos1.text", "text": "2024, vårdcentralen Solna" } },
          { "type": "DIAGNOSIS_WITH_TEXT", "id": "diagnos2",
            "diagnosis": { "type": "DIAGNOSIS", "id": "diagnos2.diagnos", "terminology": "ICD_10_SE", "code": null, "description": null },
            "text":      { "type": "TEXT",      "id": "diagnos2.text", "text": null } },
          { "type": "DIAGNOSIS_WITH_TEXT", "id": "diagnos3",
            "diagnosis": { "type": "DIAGNOSIS", "id": "diagnos3.diagnos", "terminology": "ICD_10_SE", "code": null, "description": null },
            "text":      { "type": "TEXT",      "id": "diagnos3.text", "text": null } }
        ]
      }
      """;

  private final ObjectMapper objectMapper = JsonMapper.builder().build();

  @Test
  void shallDeserializeConfigIntoDiagnosesWithTextConfig() {
    final var config = objectMapper.readValue(CONFIG_JSON, CertificateDataConfig.class);

    final var diagnosesWithText =
        assertInstanceOf(CertificateDataConfigDiagnosesWithText.class, config);
    assertAll(
        () ->
            assertEquals(
                "Diagnos eller diagnoser för de besvär som patienten anser är en arbetsskada",
                diagnosesWithText.getText()),
        () ->
            assertEquals(
                List.of(DiagnosesTerminology.builder().id("ICD_10_SE").label("ICD-10-SE").build()),
                diagnosesWithText.getTerminology()),
        () -> assertEquals("När och var ställdes diagnosen?", diagnosesWithText.getTextLabel()),
        () -> assertEquals(50, diagnosesWithText.getTextLimit()),
        () ->
            assertEquals(
                List.of(listItem("diagnos1"), listItem("diagnos2"), listItem("diagnos3")),
                diagnosesWithText.getList()));
  }

  @Test
  void shallDeserializeValueIntoDiagnosisWithTextListOfDiagnosisAndTextLeaves() {
    final var value = objectMapper.readValue(VALUE_JSON, CertificateDataValue.class);

    final var list = assertInstanceOf(CertificateDataValueDiagnosisWithTextList.class, value);
    assertAll(
        () -> assertEquals(3, list.getList().size()),
        () ->
            assertEquals(
                CertificateDataValueDiagnosisWithText.builder()
                    .id("diagnos1")
                    .diagnosis(
                        CertificateDataValueDiagnosis.builder()
                            .id("diagnos1.diagnos")
                            .terminology("ICD_10_SE")
                            .code("M545")
                            .description("Ländryggssmärta")
                            .build())
                    .text(
                        CertificateDataValueText.builder()
                            .id("diagnos1.text")
                            .text("2024, vårdcentralen Solna")
                            .build())
                    .build(),
                list.getList().get(0)),
        () ->
            assertEquals(
                CertificateDataValueDiagnosisWithText.builder()
                    .id("diagnos2")
                    .diagnosis(
                        CertificateDataValueDiagnosis.builder()
                            .id("diagnos2.diagnos")
                            .terminology("ICD_10_SE")
                            .build())
                    .text(CertificateDataValueText.builder().id("diagnos2.text").build())
                    .build(),
                list.getList().get(1)));
  }

  private static DiagnosesWithTextListItem listItem(String id) {
    return DiagnosesWithTextListItem.builder()
        .id(id)
        .diagnosisId(id + ".diagnos")
        .textId(id + ".text")
        .build();
  }
}
