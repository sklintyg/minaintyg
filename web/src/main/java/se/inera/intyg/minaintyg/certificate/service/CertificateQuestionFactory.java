package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextList;
import se.inera.intyg.minaintyg.util.HTMLUtility;

public class CertificateQuestionFactory {

  // TODO: Maybe split up into several factories

  public static String category(CertificateCategory category) {
    final var questions = HTMLUtility.fromList(category.getQuestions(),
        CertificateQuestionFactory::question);
    final var title = categoryTitle(category.getTitle());

    return HTMLFactory.section(
        HTMLUtility.join(title, questions)
    );
  }

  public static String question(CertificateQuestion question) {
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

  // TODO: Should some of the logic for textlist be refactored to HTMLFactory?
  private static String textList(CertificateQuestionTextList question) {
    final var content = HTMLUtility.fromMap(
        question.getValues(),
        value -> textListItem(value.getValue(), value.getKey())
    );

    return question(question, content);
  }

  private static String text(CertificateQuestionText question) {
    return question(question, HTMLFactory.p(question.getValue()));
  }

  private static String table(CertificateQuestionTable question) {
    final var content = HTMLFactory.table(question.getValues(), question.getHeadings());
    return question(question, content);
  }

  private static String list(CertificateQuestionList question) {
    final var content = HTMLFactory.ul(question.getValues());
    return question(question, content);
  }

  private static String questionTitle(String title) {
    return HTMLFactory.h3(title);
  }

  private static String categoryTitle(String title) {
    return HTMLFactory.h4(title);
  }

  private static String question(String title, String content) {
    return HTMLUtility.join(questionTitle(title), content);
  }

  private static String question(CertificateQuestion question, String content) {
    return question(question.getTitle(), content);
  }

  private static String textListItem(String value, String title) {
    return HTMLUtility.join(HTMLFactory.h4(title), HTMLFactory.p(value));
  }

}
