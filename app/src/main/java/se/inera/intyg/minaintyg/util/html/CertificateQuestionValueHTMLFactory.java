package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.HTMLTextFactory.h4;
import static se.inera.intyg.minaintyg.util.html.HTMLTextFactory.p;
import static se.inera.intyg.minaintyg.util.html.HTMLUtility.fromList;
import static se.inera.intyg.minaintyg.util.html.HTMLUtility.join;

import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueGeneralTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueItemList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;

public class CertificateQuestionValueHTMLFactory {

  private CertificateQuestionValueHTMLFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String text(CertificateQuestionValueText question) {
    return p(question.getValue());
  }

  public static String table(CertificateQuestionValueTable question) {
    return HTMLTableFactory.table(question.getValues(), question.getHeadings());
  }

  public static String table(CertificateQuestionValueGeneralTable question) {
    return HTMLTableFactory.table(question.getValues());
  }

  public static String list(CertificateQuestionValueList question) {
    return HTMLListFactory.ul(question.getValues());
  }

  public static String itemList(CertificateQuestionValueItemList question) {
    return fromList(
        question.getValues(),
        valueItem -> join(
            h4(valueItem.getLabel()),
            p(valueItem.getValue())
        )
    );
  }
}
