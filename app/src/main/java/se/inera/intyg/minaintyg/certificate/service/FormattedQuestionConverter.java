package se.inera.intyg.minaintyg.certificate.service;

import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.list;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.table;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionValueHTMLFactory.text;

import java.util.function.Function;
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
        HTMLUtility.fromList(
            question.getSubQuestions(),
            this::convertSubQuestion
        )
    );
  }

  private String convertQuestion(CertificateQuestion question) {
    return question(question, this::questionTitle, this::questionLabel);
  }

  private String convertSubQuestion(CertificateQuestion question) {
    return question(question, this::subQuestionTitle, this::subQuestionLabel);
  }

  private String question(CertificateQuestion question, Function<String, String> getTitle,
      Function<String, String> getLabel) {
    return HTMLUtility.join(
        getTitle.apply(question.getTitle()),
        getLabel.apply(question.getLabel()),
        value(question.getValue())
    );
  }

  private String questionTitle(String title) {
    return HTMLTextFactory.h3(title);
  }

  private String subQuestionTitle(String title) {
    return HTMLTextFactory.h4(title);
  }

  private String questionLabel(String label) {
    return HTMLTextFactory.h4(label);
  }

  private String subQuestionLabel(String label) {
    return HTMLTextFactory.h5(label);
  }

  private String value(CertificateQuestionValue value) {
    return switch (value.getType()) {
      case TEXT -> text((CertificateQuestionValueText) value);
      case LIST -> list((CertificateQuestionValueList) value);
      case TABLE -> table((CertificateQuestionValueTable) value);
    };

  }
}
