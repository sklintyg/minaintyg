package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;

@ExtendWith(MockitoExtension.class)
class FormattedCategoryConverterTest {

  @Mock
  FormattedQuestionConverter formattedQuestionConverter;

  @InjectMocks
  FormattedCategoryConverter formattedCategoryConverter;

  @BeforeEach
  void setup() {
    when(formattedQuestionConverter.convert(any(CertificateQuestion.class)))
        .thenReturn("<h3 className=\"ids-heading-3\">Question title</h3><p>text</p>");
  }

  @Test
  void shouldReturnCategory() {
    final var result = formattedCategoryConverter.convert(CertificateCategory
        .builder()
        .title("Category title")
        .questions(
            List.of(CertificateQuestion
                .builder()
                .build()
            )
        )
        .build()
    );

    assertEquals(
        "<section><h2 className=\"ids-heading-2\">Category title</h2><h3 className=\"ids-heading-3\">Question title</h3><p>text</p></section>",
        result);
  }
}