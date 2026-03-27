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

public enum CertificateDataValueType {
  BOOLEAN,
  TEXT,
  DATE,
  DATE_LIST,
  DATE_RANGE,
  DATE_RANGE_LIST,
  CODE_LIST,
  CODE,
  DIAGNOSIS_LIST,
  DIAGNOSIS,
  ICF,
  UNKOWN,
  UNCERTAIN_DATE,
  CAUSE_OF_DEATH_LIST,
  MEDICAL_INVESTIGATION_LIST,
  MEDICAL_INVESTIGATION,
  VISUAL_ACUITIES,
  DOUBLE,
  VISUAL_ACUITY,
  VIEW_TEXT,
  VIEW_LIST,
  VIEW_TABLE,
  VIEW_ROW,
  YEAR,
  INTEGER,
  CAUSE_OF_DEATH
}
