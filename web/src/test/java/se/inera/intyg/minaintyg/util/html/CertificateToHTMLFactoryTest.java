package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionText;

class CertificateToHTMLFactoryTest {

  @Test
  void shouldReturnCategory() {
    final var result = CertificateToHTMLFactory.category(CertificateCategory
        .builder()
        .title("Category title")
        .questions(
            List.of(CertificateQuestionText
                .builder()
                .title("Question title")
                .value("text")
                .build()
            )
        )
        .build()
    );

    assertEquals(
        "<section><h2 className=\"ids-heading-2\">Category title</h2><h3 className=\"ids-heading-3\">Question title</h3><p>text</p></section>",
        result);
  }

  @Test
  void shouldReturnCertificate() {
    final var result = CertificateToHTMLFactory.certificate("content");

    assertEquals("<article className=\"ids-certificate\">content</article>", result);
  }

}