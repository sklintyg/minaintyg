package se.inera.intyg.minaintyg.util.html;

import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;

public class CertificateQuestionValueHTMLFactory {

  private CertificateQuestionValueHTMLFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String text(CertificateQuestionValueText question) {
    return HTMLTextFactory.p(question.getValue());
  }

  public static String table(CertificateQuestionValueTable question) {
    return HTMLTableFactory.table(question.getValues(), question.getHeadings());
  }

  public static String list(CertificateQuestionValueList question) {
    return HTMLListFactory.ul(question.getValues());
  }
}
