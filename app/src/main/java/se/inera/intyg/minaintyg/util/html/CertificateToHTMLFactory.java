package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.CertificateQuestionHTMLFactory.list;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionHTMLFactory.table;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionHTMLFactory.text;
import static se.inera.intyg.minaintyg.util.html.CertificateQuestionHTMLFactory.textList;

import se.inera.intyg.minaintyg.certificate.legacy.CertificateCategory;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestion;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionList;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionTable;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionText;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionTextList;

public class CertificateToHTMLFactory {

  public static String certificate(String content) {
    return HTMLFactory.article(content);
  }

  public static String category(CertificateCategory category) {
    final var questions = HTMLUtility.fromList(
        category.getQuestions(),
        CertificateToHTMLFactory::question
    );
    final var title = categoryTitle(category.getTitle());

    return HTMLFactory.section(
        HTMLUtility.join(title, questions)
    );
  }

  private static String categoryTitle(String title) {
    return HTMLTextFactory.h2(title);
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
