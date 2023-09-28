package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;

@ExtendWith(MockitoExtension.class)
class ConvertCertificateServiceTest {

  private static final String CATEGORY_CONFIG_TYPE = "categoryConfig";
  private static final String CATEGORY_CONFIG_TYPE_WITHOUT_TEXT = "categoryWithoutTextConfig";
  private static final String TEXT_CONFIG_TYPE = "textConfig";
  private static final String TEXT_VALUE_TYPE = "textValue";
  private static final String TEXT_NO_VALUE_TYPE = "textNoValue";
  private static final String BOOLEAN_VALUE_TYPE_TRUE = "booleanValueTrue";
  private static final String BOOLEAN_VALUE_TYPE_FALSE = "booleanFalse";
  private static final String BOOLEAN_NO_VALUE_TYPE = "booleanNoValue";
  private static final String BOOLEAN_CONFIG_TYPE = "booleanConfig";
  private static final String TITLE = "title";
  private static final String TEXT_VALUE = "textValue";
  private static final String NO_VALUE = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";
  private static final Map<String, CertificateDataConfig> configMap = Map.of(
      CATEGORY_CONFIG_TYPE, CertificateDataConfigCategory.builder()
          .text(TITLE)
          .build(),
      CATEGORY_CONFIG_TYPE_WITHOUT_TEXT, CertificateDataConfigCategory.builder()
          .build(),
      TEXT_CONFIG_TYPE, CertificateDataConfigTextArea.builder()
          .text(TEXT_VALUE)
          .build(),
      BOOLEAN_CONFIG_TYPE, CertificateDataConfigRadioBoolean.builder()
          .text(TEXT_VALUE)
          .build()
  );

  private static final Map<String, CertificateDataValue> valueMap = Map.of(
      TEXT_VALUE_TYPE, CertificateDataTextValue.builder()
          .text(TEXT_VALUE)
          .build(),
      TEXT_NO_VALUE_TYPE, CertificateDataTextValue.builder()
          .build(),
      BOOLEAN_VALUE_TYPE_TRUE, CertificateDataValueBoolean.builder()
          .selected(true)
          .build(),
      BOOLEAN_VALUE_TYPE_FALSE, CertificateDataValueBoolean.builder()
          .selected(false)
          .build(),
      BOOLEAN_NO_VALUE_TYPE, CertificateDataValueBoolean.builder()
          .build()
  );

  private static ConvertCertificateService convertCertificateService;

  @BeforeAll
  static void beforeAll() {
    convertCertificateService = new ConvertCertificateService();
  }

  @Nested
  class CategoryTitle {

    @Test
    void shouldAddCategoryTitle() {
      final var elements = List.of(
          List.of(createElement(CATEGORY_CONFIG_TYPE, TEXT_VALUE_TYPE), createElement(
              TEXT_CONFIG_TYPE, TEXT_VALUE_TYPE)));
      final var result = convertCertificateService.convert(elements);
      assertEquals(TITLE, result.get(0).getTitle());
    }

    @Test
    void shouldAddMultipleCategoryTitle() {
      final var elements = List.of(
          List.of(createElement(CATEGORY_CONFIG_TYPE,
              TEXT_VALUE_TYPE), createElement(TEXT_CONFIG_TYPE, TEXT_VALUE_TYPE)),
          List.of(createElement(CATEGORY_CONFIG_TYPE,
              TEXT_VALUE_TYPE), createElement(TEXT_CONFIG_TYPE, TEXT_VALUE_TYPE)),
          List.of(createElement(CATEGORY_CONFIG_TYPE,
              TEXT_VALUE_TYPE), createElement(TEXT_CONFIG_TYPE, TEXT_VALUE_TYPE)));
      final var result = convertCertificateService.convert(elements);
      assertEquals(TITLE, result.get(0).getTitle());
      assertEquals(TITLE, result.get(1).getTitle());
      assertEquals(TITLE, result.get(2).getTitle());
    }

    @Test
    void shouldHandleNullTitle() {
      final var elements = List.of(
          List.of(createElement(CATEGORY_CONFIG_TYPE_WITHOUT_TEXT,
              TEXT_VALUE_TYPE), createElement(TEXT_CONFIG_TYPE, TEXT_VALUE_TYPE))
      );
      final var result = convertCertificateService.convert(elements);
      assertNull(result.get(0).getTitle());
    }
  }

  @Nested
  class ToCertificateQuestion {

    @Nested
    class TextValue {

      @Test
      void shouldConvertCertificateDataTextValue() {
        final var elements = List.of(
            List.of(createElement(CATEGORY_CONFIG_TYPE, TEXT_VALUE_TYPE),
                createElement(TEXT_CONFIG_TYPE, TEXT_VALUE_TYPE)
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .title(TEXT_VALUE)
            .value(CertificateQuestionValueText.builder()
                .value(TEXT_VALUE)
                .build())
            .build();

        final var result = convertCertificateService.convert(elements);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataTextValueWithNoValue() {
        final var elements = List.of(
            List.of(createElement(CATEGORY_CONFIG_TYPE, TEXT_VALUE_TYPE),
                createElement(TEXT_CONFIG_TYPE, TEXT_NO_VALUE_TYPE)
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .title(TEXT_VALUE)
            .value(CertificateQuestionValueText.builder()
                .value(NO_VALUE)
                .build())
            .build();

        final var result = convertCertificateService.convert(elements);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }
    }

    @Nested
    class RadioBoolean {

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithValueTrue() {
        final var elements = List.of(
            List.of(createElement(CATEGORY_CONFIG_TYPE, TEXT_VALUE_TYPE),
                createElement(BOOLEAN_CONFIG_TYPE, BOOLEAN_VALUE_TYPE_TRUE)
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .title(TEXT_VALUE)
            .value(
                CertificateQuestionValueText.builder()
                    .value(TRUE_BOOLEAN)
                    .build())
            .build();
        final var result = convertCertificateService.convert(elements);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithValueFalse() {
        final var elements = List.of(
            List.of(createElement(CATEGORY_CONFIG_TYPE, TEXT_VALUE_TYPE),
                createElement(BOOLEAN_CONFIG_TYPE, BOOLEAN_VALUE_TYPE_FALSE)
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .title(TEXT_VALUE)
            .value(
                CertificateQuestionValueText.builder()
                    .value(FALSE_BOOLEAN)
                    .build())
            .build();
        final var result = convertCertificateService.convert(elements);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithNoValue() {
        final var elements = List.of(
            List.of(createElement(CATEGORY_CONFIG_TYPE, TEXT_VALUE_TYPE),
                createElement(BOOLEAN_CONFIG_TYPE, BOOLEAN_NO_VALUE_TYPE)
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .title(TEXT_VALUE)
            .value(
                CertificateQuestionValueText.builder()
                    .value(NO_VALUE)
                    .build())
            .build();
        final var result = convertCertificateService.convert(elements);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }
    }
  }


  private static CertificateDataElement createElement(String config, String value) {
    return CertificateDataElement.builder()
        .config(configMap.get(config))
        .value(valueMap.get(value))
        .build();
  }
}
