package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;

@ExtendWith(MockitoExtension.class)
class ConvertCertificateServiceTest {

  private static final String CATEGORY_TYPE = "category";
  private static final String CATEGORY_TYPE_WITHOUT_TEXT = "categoryWithoutText";
  private static final String TEXT_TYPE = "text";
  private static final String BOOLEAN_TYPE = "boolean";
  private static final String CATEGORY_TITLE = "title";
  private static final Map<String, CertificateDataConfig> configMap = Map.of(
      CATEGORY_TYPE, CertificateDataConfigCategory.builder()
          .text(CATEGORY_TITLE)
          .build(),
      CATEGORY_TYPE_WITHOUT_TEXT, CertificateDataConfigCategory.builder()
          .build(),
      TEXT_TYPE, CertificateDataConfigTextArea.builder().build(),
      BOOLEAN_TYPE, CertificateDataConfigRadioBoolean.builder().build()
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
      final var elements = List.of(List.of(createElement(CATEGORY_TYPE), createElement(TEXT_TYPE)));
      final var result = convertCertificateService.convert(elements);
      assertEquals(CATEGORY_TITLE, result.get(0).getTitle());
    }

    @Test
    void shouldAddMultipleCategoryTitle() {
      final var elements = List.of(
          List.of(createElement(CATEGORY_TYPE), createElement(TEXT_TYPE)),
          List.of(createElement(CATEGORY_TYPE), createElement(TEXT_TYPE)),
          List.of(createElement(CATEGORY_TYPE), createElement(TEXT_TYPE)));
      final var result = convertCertificateService.convert(elements);
      assertEquals(CATEGORY_TITLE, result.get(0).getTitle());
      assertEquals(CATEGORY_TITLE, result.get(1).getTitle());
      assertEquals(CATEGORY_TITLE, result.get(2).getTitle());
    }

    @Test
    void shouldThrowIfCategoryDoesNotContainText() {
      final var elements = List.of(
          List.of(createElement(CATEGORY_TYPE_WITHOUT_TEXT), createElement(TEXT_TYPE))
      );
      assertThrows(IllegalArgumentException.class,
          () -> convertCertificateService.convert(elements)
      );
    }
  }

  private static CertificateDataElement createElement(String type) {
    return CertificateDataElement.builder()
        .config(configMap.get(type))
        .build();
  }


}