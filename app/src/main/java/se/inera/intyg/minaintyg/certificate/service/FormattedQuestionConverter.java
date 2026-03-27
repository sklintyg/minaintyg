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
package se.inera.intyg.minaintyg.certificate.service;

import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.itemList;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.list;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.table;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.text;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueGeneralTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueItemList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.util.html.HTMLTextFactory;
import se.inera.intyg.minaintyg.util.html.HTMLUtility;

@Service
@RequiredArgsConstructor
public class FormattedQuestionConverter {

  public String convert(CertificateQuestion question) {
    return HTMLUtility.join(
        convertQuestion(question),
        HTMLUtility.fromList(question.getSubQuestions(), this::convertSubQuestion));
  }

  private String convertQuestion(CertificateQuestion question) {
    if (question.getHeader() != null && !question.getHeader().isEmpty()) {
      return HTMLUtility.join(
          questionHeader(question.getHeader()),
          subQuestionTitle(question.getTitle()),
          questionLabel(question.getLabel(), question.getTitle()),
          value(question.getValue()));
    }

    return HTMLUtility.join(
        questionTitle(question.getTitle()),
        questionLabel(question.getLabel(), question.getTitle()),
        value(question.getValue()));
  }

  private String convertSubQuestion(CertificateQuestion question) {
    return HTMLUtility.join(
        subQuestionTitle(question.getTitle()),
        subQuestionLabel(question.getLabel(), question.getTitle()),
        value(question.getValue()),
        HTMLUtility.fromList(question.getSubQuestions(), this::convertSubQuestion));
  }

  private String questionHeader(String title) {
    return HTMLTextFactory.h3(title);
  }

  private String questionTitle(String title) {
    return HTMLTextFactory.h3(title);
  }

  private String subQuestionTitle(String title) {
    return HTMLTextFactory.h4(title);
  }

  private String questionLabel(String label, String title) {
    return Strings.isNullOrEmpty(title) ? HTMLTextFactory.h3(label) : HTMLTextFactory.h4(label);
  }

  private String subQuestionLabel(String label, String title) {
    return Strings.isNullOrEmpty(title) ? HTMLTextFactory.h4(label) : HTMLTextFactory.h5(label);
  }

  private String value(CertificateQuestionValue value) {
    if (value == null) {
      return null;
    }

    return switch (value.getType()) {
      case TEXT -> text((CertificateQuestionValueText) value);
      case LIST -> list((CertificateQuestionValueList) value);
      case TABLE -> table((CertificateQuestionValueTable) value);
      case GENERAL_TABLE -> table((CertificateQuestionValueGeneralTable) value);
      case ITEM_LIST -> itemList((CertificateQuestionValueItemList) value);
    };
  }
}
