package se.inera.intyg.minaintyg.integration.webcert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;

@Component
public class CategoryQuestionOrganizer {

  public List<List<CertificateDataElement>> organize(
      List<CertificateDataElement> certificateDataElements) {
    final var elementsSortedByIndex = getElementsSortedByIndex(certificateDataElements);
    final var organizedCertificateDataElements = new ArrayList<List<CertificateDataElement>>();

    elementsSortedByIndex.forEach(element -> {
      if (elementIsCategory(element)) {
        organizedCertificateDataElements.add(new ArrayList<>());
        addElementToList(organizedCertificateDataElements, element);
      } else {
        addElementToList(organizedCertificateDataElements, element);
      }
    });
    return organizedCertificateDataElements;
  }

  private static void addElementToList(
      ArrayList<List<CertificateDataElement>> organizedCertificateDataElements,
      CertificateDataElement element) {
    if (organizedCertificateDataElements.isEmpty()) {
      throw new IllegalArgumentException("Questions without related categories are not permited");
    }
    organizedCertificateDataElements.get(organizedCertificateDataElements.size() - 1)
        .add(element);
  }

  private static List<CertificateDataElement> getElementsSortedByIndex(
      List<CertificateDataElement> certificateDataElements) {
    return certificateDataElements.stream()
        .sorted(Comparator.comparingInt(CertificateDataElement::getIndex))
        .toList();
  }

  private static boolean elementIsCategory(CertificateDataElement element) {
    return element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }
}
