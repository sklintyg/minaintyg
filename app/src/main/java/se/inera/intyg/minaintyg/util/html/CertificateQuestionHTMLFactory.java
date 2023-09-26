package se.inera.intyg.minaintyg.util.html;

import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestion;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionList;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionTable;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionText;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateQuestionTextList;

public class CertificateQuestionHTMLFactory {

  public static String textList(CertificateQuestionTextList question) {
    return question(question, HTMLListFactory.textList(question.getValues()));
  }

  public static String text(CertificateQuestionText question) {
    return question(question, HTMLTextFactory.p(question.getValue()));
  }

  public static String table(CertificateQuestionTable question) {
    final var content = HTMLTableFactory.table(question.getValues(), question.getHeadings());
    return question(question, content);
  }

  public static String list(CertificateQuestionList question) {
    final var content = HTMLListFactory.ul(question.getValues());
    return question(question, content);
  }

  private static String questionTitle(String title) {
    return HTMLTextFactory.h3(title);
  }

  private static String question(String title, String content) {
    return HTMLUtility.join(questionTitle(title), content);
  }

  private static String question(CertificateQuestion question, String content) {
    return question(question.getTitle(), content);
  }
}
