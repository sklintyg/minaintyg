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
  void shouldConvertCategoryHeading() {
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
        "Category title",
        result.getHeading());
  }

  @Test
  void shouldConvertCategoryBody() {
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
        "<h3 className=\"ids-heading-3\">Question title</h3><p>text</p>",
        result.getBody());
  }

  @Test
  void shouldConvertCategoryBodyForSeveralQuestions() {
    final var result = formattedCategoryConverter.convert(CertificateCategory
        .builder()
        .title("Category title")
        .questions(
            List.of(CertificateQuestion
                    .builder()
                    .build(),
                CertificateQuestion
                    .builder()
                    .build()
            )
        )
        .build()
    );

    assertEquals(
        "<h3 className=\"ids-heading-3\">Question title</h3><p>text</p><h3 className=\"ids-heading-3\">Question title</h3><p>text</p>",
        result.getBody());
  }
}