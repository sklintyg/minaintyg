package se.inera.intyg.minaintyg.util.html;

import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionListValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTableValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextValue;

public class CertificateQuestionValueHTMLFactory {

  public static String text(CertificateQuestionTextValue question) {
    return HTMLTextFactory.p(question.getValue());
  }

  public static String table(CertificateQuestionTableValue question) {
    return HTMLTableFactory.table(question.getValues(), question.getHeadings());
  }

  public static String list(CertificateQuestionListValue question) {
    return HTMLListFactory.ul(question.getValues());
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
