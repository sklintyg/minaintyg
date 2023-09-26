package se.inera.intyg.minaintyg.certificate.service;

import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.list;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.table;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.text;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionListValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTableValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateSubQuestion;
import se.inera.intyg.minaintyg.util.html.HTMLTextFactory;
import se.inera.intyg.minaintyg.util.html.HTMLUtility;

@Service
@RequiredArgsConstructor
public class FormattedQuestionConverter {

  public String convert(CertificateQuestion question) {
    return HTMLUtility.join(
        questionTitle(question.getTitle()),
        value(question.getValue()),
        question.getSubQuestions()
            .stream()
            .map(this::convertSubQuestion)
            .collect(Collectors.joining())
    );
  }

  private String convertSubQuestion(CertificateSubQuestion subQuestion) {
    return HTMLUtility.join(
        questionTitle(subQuestion.getTitle()),
        value(subQuestion.getValue())
    );
  }

  private String questionTitle(String title) {
    return HTMLTextFactory.h3(title);
  }

  private String value(CertificateQuestionValue value) {
    switch (value.getType()) {
      case TEXT:
        return text((CertificateQuestionTextValue) value);
      case LIST:
        return list((CertificateQuestionListValue) value);
      case TABLE:
        return table((CertificateQuestionTableValue) value);
    }

    return "";
  }
}
