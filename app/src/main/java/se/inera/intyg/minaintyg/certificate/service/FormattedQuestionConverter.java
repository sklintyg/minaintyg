package se.inera.intyg.minaintyg.certificate.service;

import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.list;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.table;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.text;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
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
        question.getSubQuestions()
            .stream()
            .map(this::convertQuestion)
            .collect(Collectors.joining())
    );
  }

  private String convertQuestion(CertificateQuestion question) {
    return HTMLUtility.join(
        questionTitle(question.getTitle()),
        questionLabel(question.getLabel()),
        value(question.getValue())
    );
  }

  private String questionTitle(String title) {
    return HTMLTextFactory.h3(title);
  }

  private String questionLabel(String label) {

    return HTMLTextFactory.h4(label);
  }

  private String value(CertificateQuestionValue value) {
    return switch (value.getType()) {
      case TEXT -> text((CertificateQuestionValueText) value);
      case LIST -> list((CertificateQuestionValueList) value);
      case TABLE -> table((CertificateQuestionValueTable) value);
    };

  }
}
