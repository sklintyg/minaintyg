package se.inera.intyg.minaintyg.util;

import static se.inera.intyg.minaintyg.util.CertificateQuestionHTMLFactory.list;
import static se.inera.intyg.minaintyg.util.CertificateQuestionHTMLFactory.table;
import static se.inera.intyg.minaintyg.util.CertificateQuestionHTMLFactory.text;
import static se.inera.intyg.minaintyg.util.CertificateQuestionHTMLFactory.textList;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextList;

public class CertificateToHTMLFactory {


  public static String certificate(String content) {
    return HTMLFactory.article(content);
  }

  public static String category(CertificateCategory category) {
    final var questions = HTMLUtility.fromList(category.getQuestions(),
        CertificateToHTMLFactory::question);
    final var title = categoryTitle(category.getTitle());

    return HTMLFactory.section(
        HTMLUtility.join(title, questions)
    );
  }

  private static String categoryTitle(String title) {
    return HTMLFactory.h2(title);
  }

  private static String question(CertificateQuestion question) {
    switch (question.getType()) {
      case TEXT:
        return text((CertificateQuestionText) question);
      case LIST:
        return list((CertificateQuestionList) question);
      case TABLE:
        return table((CertificateQuestionTable) question);
      case TEXT_LIST:
        return textList((CertificateQuestionTextList) question);
    }

    return "";
  }

}
