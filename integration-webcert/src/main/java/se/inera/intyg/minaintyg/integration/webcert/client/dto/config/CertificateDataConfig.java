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
package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @Type(value = CertificateDataConfigCategory.class, name = "CATEGORY"),
  @Type(value = CertificateDataConfigTextArea.class, name = "UE_TEXTAREA"),
  @Type(value = CertificateDataConfigTextField.class, name = "UE_TEXTFIELD"),
  @Type(value = CertificateDataConfigTypeAhead.class, name = "UE_TYPE_AHEAD"),
  @Type(value = CertificateDataConfigDate.class, name = "UE_DATE"),
  @Type(value = CertificateDataConfigMessage.class, name = "UE_MESSAGE"),
  @Type(value = CertificateDataConfigHeader.class, name = "UE_HEADER"),
  @Type(value = CertificateDataConfigUncertainDate.class, name = "UE_UNCERTAIN_DATE"),
  @Type(value = CertificateDataConfigCheckboxBoolean.class, name = "UE_CHECKBOX_BOOLEAN"),
  @Type(value = CertificateDataConfigRadioBoolean.class, name = "UE_RADIO_BOOLEAN"),
  @Type(value = CertificateDataConfigRadioMultipleCode.class, name = "UE_RADIO_MULTIPLE_CODE"),
  @Type(
      value = CertificateDataConfigRadioMultipleCodeOptionalDropdown.class,
      name = "UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN"),
  @Type(
      value = CertificateDataConfigCheckboxMultipleDate.class,
      name = "UE_CHECKBOX_MULTIPLE_DATE"),
  @Type(
      value = CertificateDataConfigCheckboxMultipleCode.class,
      name = "UE_CHECKBOX_MULTIPLE_CODE"),
  @Type(
      value = CertificateDataConfigCheckboxDateRangeList.class,
      name = "UE_CHECKBOX_DATE_RANGE_LIST"),
  @Type(value = CertificateDataConfigDiagnoses.class, name = "UE_DIAGNOSES"),
  @Type(value = CertificateDataConfigDropdown.class, name = "UE_DROPDOWN"),
  @Type(value = CertificateDataConfigIcf.class, name = "UE_ICF"),
  @Type(value = CertificateDataConfigCauseOfDeath.class, name = "UE_CAUSE_OF_DEATH"),
  @Type(value = CertificateDataConfigCauseOfDeathList.class, name = "UE_CAUSE_OF_DEATH_LIST"),
  @Type(value = CertificateDataConfigMedicalInvestigation.class, name = "UE_MEDICAL_INVESTIGATION"),
  @Type(value = CertificateDataConfigVisualAcuity.class, name = "UE_VISUAL_ACUITY"),
  @Type(value = CertificateDataConfigViewText.class, name = "UE_VIEW_TEXT"),
  @Type(value = CertificateDataConfigViewList.class, name = "UE_VIEW_LIST"),
  @Type(value = CertificateDataConfigViewTable.class, name = "UE_VIEW_TABLE"),
  @Type(value = CertificateDataConfigYear.class, name = "UE_YEAR"),
  @Type(value = CertificateDataConfigInteger.class, name = "UE_INTEGER"),
  @Type(value = CertificateDataConfigDateRange.class, name = "UE_DATE_RANGE")
})
public interface CertificateDataConfig {

  CertificateDataConfigType getType();

  String getHeader();

  String getLabel();

  String getIcon();

  String getText();

  String getDescription();

  Accordion getAccordion();

  Message getMessage();
}
