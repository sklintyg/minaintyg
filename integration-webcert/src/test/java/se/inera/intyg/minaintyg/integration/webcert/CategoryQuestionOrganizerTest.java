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
        createElement(TEXT_TYPE, 1, "1", "0"),
        createElement(BOOLEAN_TYPE, 4, "4", "0"),
        createElement(TEXT_TYPE, 2, "2", "0"),
        createElement(BOOLEAN_TYPE, 3, "3", "0"),
        createElement(CATEGORY_TYPE, 0, "0", null)
    );
    final var expectedResult = List.of(
        List.of(
            createElement(CATEGORY_TYPE, 0, "0", null),
            createElement(TEXT_TYPE, 1, "1", "0"),
            createElement(TEXT_TYPE, 2, "2", "0"),
            createElement(BOOLEAN_TYPE, 3, "3", "0"),
            createElement(BOOLEAN_TYPE, 4, "4", "0")
        )
    );
    final var result = categoryQuestionOrganizer.organize(certificateDataElements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldGroupElementsByCategory() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1, "1", "0"),
        createElement(BOOLEAN_TYPE, 4, "4", "2"),
        createElement(CATEGORY_TYPE, 2, "2", null),
        createElement(BOOLEAN_TYPE, 3, "3", "2"),
        createElement(CATEGORY_TYPE, 0, "0", null)
    );
    final var expectedResult = List.of(
        List.of(
            createElement(CATEGORY_TYPE, 0, "0", null),
            createElement(TEXT_TYPE, 1, "1", "0")
        ),
        List.of(
            createElement(CATEGORY_TYPE, 2, "2", null),
            createElement(BOOLEAN_TYPE, 3, "3", "2"),
            createElement(BOOLEAN_TYPE, 4, "4", "2")
        )
    );
    final var result = categoryQuestionOrganizer.organize(certificateDataElements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldGroupElementsByMultipleCategories() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1, "1", "0"),
        createElement(BOOLEAN_TYPE, 4, "4", "2"),
        createElement(CATEGORY_TYPE, 2, "2", null),
        createElement(BOOLEAN_TYPE, 3, "3", "2"),
        createElement(CATEGORY_TYPE, 0, "0", null),
        createElement(CATEGORY_TYPE, 5, "5", null),
        createElement(BOOLEAN_TYPE, 6, "6", "5"),
        createElement(CATEGORY_TYPE, 7, "7", null),
        createElement(BOOLEAN_TYPE, 8, "8", "7")
    );
    final var expectedResult = List.of(
        List.of(
            createElement(CATEGORY_TYPE, 0, "0", null),
            createElement(TEXT_TYPE, 1, "1", "0")
        ),
        List.of(
            createElement(CATEGORY_TYPE, 2, "2", null),
            createElement(BOOLEAN_TYPE, 3, "3", "2"),
            createElement(BOOLEAN_TYPE, 4, "4", "2")
        ),
        List.of(
            createElement(CATEGORY_TYPE, 5, "5", null),
            createElement(BOOLEAN_TYPE, 6, "6", "5")
        ),
        List.of(
            createElement(CATEGORY_TYPE, 7, "7", null),
            createElement(BOOLEAN_TYPE, 8, "8", "7")
        )
    );
    final var result = categoryQuestionOrganizer.organize(certificateDataElements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldThrowIfQuestionDoesNotHaveACategoryParent() {
    final var certificateDataElements = List.of(
        createElement(TEXT_TYPE, 1, "1", null),
        createElement(BOOLEAN_TYPE, 4, "1", null)
    );
    assertThrows(IllegalArgumentException.class,
        () -> categoryQuestionOrganizer.organize(certificateDataElements));
  }

  private static CertificateDataElement createElement(String type, int index, String id,
      String parent) {
    return CertificateDataElement.builder()
        .id(id)
        .parent(parent)
        .index(index)
        .config(configMap.get(type))
        .build();
  }
}
