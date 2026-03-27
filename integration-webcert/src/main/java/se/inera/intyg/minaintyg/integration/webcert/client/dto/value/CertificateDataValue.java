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
package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @Type(value = CertificateDataValueText.class, name = "TEXT"),
  @Type(value = CertificateDataValueBoolean.class, name = "BOOLEAN"),
  @Type(value = CertificateDataValueDateList.class, name = "DATE_LIST"),
  @Type(value = CertificateDataValueDate.class, name = "DATE"),
  @Type(value = CertificateDataUncertainDateValue.class, name = "UNCERTAIN_DATE"),
  @Type(value = CertificateDataValueDateRangeList.class, name = "DATE_RANGE_LIST"),
  @Type(value = CertificateDataValueDateRange.class, name = "DATE_RANGE"),
  @Type(value = CertificateDataValueDiagnosisList.class, name = "DIAGNOSIS_LIST"),
  @Type(value = CertificateDataValueDiagnosis.class, name = "DIAGNOSIS"),
  @Type(value = CertificateDataValueCodeList.class, name = "CODE_LIST"),
  @Type(value = CertificateDataValueCode.class, name = "CODE"),
  @Type(value = CertificateDataIcfValue.class, name = "ICF"),
  @Type(value = CertificateDataValueCauseOfDeath.class, name = "CAUSE_OF_DEATH"),
  @Type(value = CertificateDataValueCauseOfDeathList.class, name = "CAUSE_OF_DEATH_LIST"),
  @Type(value = CertificateDataValueMedicalInvestigation.class, name = "MEDICAL_INVESTIGATION"),
  @Type(
      value = CertificateDataValueMedicalInvestigationList.class,
      name = "MEDICAL_INVESTIGATION_LIST"),
  @Type(value = CertificateDataValueVisualAcuities.class, name = "VISUAL_ACUITIES"),
  @Type(value = CertificateDataValueVisualAcuity.class, name = "VISUAL_ACUITY"),
  @Type(value = CertificateDataValueDouble.class, name = "DOUBLE"),
  @Type(value = CertificateDataValueViewText.class, name = "VIEW_TEXT"),
  @Type(value = CertificateDataValueViewList.class, name = "VIEW_LIST"),
  @Type(value = CertificateDataValueViewTable.class, name = "VIEW_TABLE"),
  @Type(value = CertificateDataValueViewRow.class, name = "VIEW_ROW"),
  @Type(value = CertificateDataValueYear.class, name = "YEAR"),
  @Type(value = CertificateDataValueInteger.class, name = "INTEGER")
})
public interface CertificateDataValue {

  CertificateDataValueType getType();
}
