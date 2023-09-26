package se.inera.intyg.minaintyg.util.html;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;

public class CertificateQuestionValueHTMLFactory {

  public static String text(CertificateQuestionValueText question) {
    return HTMLTextFactory.p(question.getValue());
  }

  public static String table(CertificateQuestionValueTable question) {
    return HTMLTableFactory.table(question.getValues(), question.getHeadings());
  }

  public static String list(CertificateQuestionValueList question) {
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
