package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;

class CategoryQuestionOrganizerTest {

  private final CategoryQuestionOrganizer categoryQuestionOrganizer = new CategoryQuestionOrganizer();
  private static final String CATEGORY_TYPE = "category";
  private static final String TEXT_TYPE = "text";
  private static final String BOOLEAN_TYPE = "boolean";

  private static final Map<String, CertificateDataConfig> configMap = Map.of(
      CATEGORY_TYPE, CertificateDataConfigCategory.builder().build(),
      TEXT_TYPE, CertificateDataConfigTextArea.builder().build(),
      BOOLEAN_TYPE, CertificateDataConfigRadioBoolean.builder().build()
  );

  @Test
  void shouldOrganizeElementsByIndex() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1),
        createElement(BOOLEAN_TYPE, 4),
        createElement(TEXT_TYPE, 2),
        createElement(BOOLEAN_TYPE, 3),
        createElement(CATEGORY_TYPE, 0)
    );
    final var expectedResult = List.of(
        List.of(
            createElement(CATEGORY_TYPE, 0),
            createElement(TEXT_TYPE, 1),
            createElement(TEXT_TYPE, 2),
            createElement(BOOLEAN_TYPE, 3),
            createElement(BOOLEAN_TYPE, 4)
        )
    );
    final var result = categoryQuestionOrganizer.organize(certificateDataElements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldGroupElementsByCategory() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1),
        createElement(BOOLEAN_TYPE, 4),
        createElement(CATEGORY_TYPE, 2),
        createElement(BOOLEAN_TYPE, 3),
        createElement(CATEGORY_TYPE, 0)
    );
    final var expectedResult = List.of(
        List.of(
            createElement(CATEGORY_TYPE, 0),
            createElement(TEXT_TYPE, 1)
        ),
        List.of(
            createElement(CATEGORY_TYPE, 2),
            createElement(BOOLEAN_TYPE, 3),
            createElement(BOOLEAN_TYPE, 4)
        )
    );
    final var result = categoryQuestionOrganizer.organize(certificateDataElements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldGroupElementsByMultipleCategories() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1),
        createElement(BOOLEAN_TYPE, 4),
        createElement(CATEGORY_TYPE, 2),
        createElement(BOOLEAN_TYPE, 3),
        createElement(CATEGORY_TYPE, 0),
        createElement(CATEGORY_TYPE, 5),
        createElement(BOOLEAN_TYPE, 6),
        createElement(CATEGORY_TYPE, 7),
        createElement(BOOLEAN_TYPE, 8)
    );
    final var expectedResult = List.of(
        List.of(
            createElement(CATEGORY_TYPE, 0),
            createElement(TEXT_TYPE, 1)
        ),
        List.of(
            createElement(CATEGORY_TYPE, 2),
            createElement(BOOLEAN_TYPE, 3),
            createElement(BOOLEAN_TYPE, 4)
        ),
        List.of(
            createElement(CATEGORY_TYPE, 5),
            createElement(BOOLEAN_TYPE, 6)
        ),
        List.of(
            createElement(CATEGORY_TYPE, 7),
            createElement(BOOLEAN_TYPE, 8)
        )
    );
    final var result = categoryQuestionOrganizer.organize(certificateDataElements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldThrowIfQuestionDoesNotHaveACategoryParent() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1),
        createElement(BOOLEAN_TYPE, 4)
    );
    assertThrows(IllegalArgumentException.class,
        () -> categoryQuestionOrganizer.organize(certificateDataElements));
  }

  private static CertificateDataElement createElement(String type, int index) {
    return CertificateDataElement.builder()
        .index(index)
        .config(configMap.get(type))
        .build();
  }
}
