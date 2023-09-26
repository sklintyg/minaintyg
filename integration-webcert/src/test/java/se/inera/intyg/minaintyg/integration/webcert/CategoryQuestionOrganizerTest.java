package se.inera.intyg.minaintyg.integration.webcert;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;

class CategoryQuestionOrganizerTest {

  private final CategoryQuestionOrganizer categoryQuestionOrganizer = new CategoryQuestionOrganizer();

  @Test
  void shouldGroupDataElementsToOneCategoryWithQuestions() {
    CertificateDataElement.builder()
        .id("test")
        .build();

  }
}