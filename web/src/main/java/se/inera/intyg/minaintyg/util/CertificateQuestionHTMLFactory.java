package se.inera.intyg.minaintyg.util;

import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextList;

public class CertificateQuestionHTMLFactory {

  public static String textList(CertificateQuestionTextList question) {
    return question(question, HTMLFactory.textList(question.getValues()));
  }

  public static String text(CertificateQuestionText question) {
    return question(question, HTMLFactory.p(question.getValue()));
  }

  public static String table(CertificateQuestionTable question) {
    final var content = HTMLFactory.table(question.getValues(), question.getHeadings());
    return question(question, content);
  }

  public static String list(CertificateQuestionList question) {
    final var content = HTMLFactory.ul(question.getValues());
    return question(question, content);
  }

  private static String questionTitle(String title) {
    return HTMLFactory.h3(title);
  }

  private static String question(String title, String content) {
    return HTMLUtility.join(questionTitle(title), content);
  }

  private static String question(CertificateQuestion question, String content) {
    return question(question.getTitle(), content);
  }
}
