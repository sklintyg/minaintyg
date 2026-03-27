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
package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.HTMLTextFactory.h4;
import static se.inera.intyg.minaintyg.util.html.HTMLTextFactory.p;
import static se.inera.intyg.minaintyg.util.html.HTMLUtility.fromList;
import static se.inera.intyg.minaintyg.util.html.HTMLUtility.join;

import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueGeneralTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueItemList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;

public class CertificateQuestionValueHTMLFactory {

  private CertificateQuestionValueHTMLFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String text(CertificateQuestionValueText question) {
    return p(question.getValue());
  }

  public static String table(CertificateQuestionValueTable question) {
    return HTMLTableFactory.table(question.getValues(), question.getHeadings());
  }

  public static String table(CertificateQuestionValueGeneralTable question) {
    return HTMLTableFactory.generalTable(question.getValues(), question.getHeadings());
  }

  public static String list(CertificateQuestionValueList question) {
    return HTMLListFactory.ul(question.getValues());
  }

  public static String itemList(CertificateQuestionValueItemList question) {
    return fromList(
        question.getValues(), valueItem -> join(h4(valueItem.getLabel()), p(valueItem.getValue())));
  }
}
