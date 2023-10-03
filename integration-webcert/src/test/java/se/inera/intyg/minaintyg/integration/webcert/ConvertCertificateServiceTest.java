package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;

@ExtendWith(MockitoExtension.class)
class ConvertCertificateServiceTest {

  private static final String TITLE = "title";
  private static final String TEXT_VALUE = "textValue";

  private static final CertificateResponseDTO CERTIFICATE_RESPONSE_DTO = CertificateResponseDTO.builder()
      .certificate(
          CertificateDTO.builder()
              .data(Collections.emptyMap())
              .build())
      .build();
  @Mock
  private CategoryQuestionOrganizer categoryQuestionOrganizer;
  @Mock
  private QuestionConverter questionConverter;
  @InjectMocks
  private ConvertCertificateService convertCertificateService;

  @Nested
  class CategoryTitle {

    @Test
    void shouldAddCategoryTitle() {
      final var elements = List.of(
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null)));
      when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
      final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
      assertEquals(TITLE, result.get(0).getTitle());
    }

    @Test
    void shouldAddMultipleCategoryTitle() {
      final var elements = List.of(
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null)),
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null)),
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null))
      );
      when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
      final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
      assertEquals(TITLE, result.get(0).getTitle());
      assertEquals(TITLE, result.get(1).getTitle());
      assertEquals(TITLE, result.get(2).getTitle());
    }

    @Test
    void shouldHandleNullTitle() {
      final var elements = List.of(
          List.of(createElement(CertificateDataConfigCategory.builder().build(), null)
          )
      );
      when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
      final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
      assertNull(result.get(0).getTitle());
    }
  }

  @Test
  void shouldConvertQuestions() {
    final var element = createElement(
        CertificateDataConfigTextArea.builder().build(),
        CertificateDataTextValue.builder()
            .text(TEXT_VALUE)
            .build()
    );
    final var elements = List.of(
        List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
            element
        )
    );

    final var expectedResult = CertificateQuestion.builder()
        .value(CertificateQuestionValueText.builder()
            .value(TEXT_VALUE)
            .build())
        .build();

    when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
    when(questionConverter.convert(element)).thenReturn(expectedResult);
    final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
    assertEquals(expectedResult, result.get(0).getQuestions().get(0));
  }

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}
