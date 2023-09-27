package se.inera.intyg.minaintyg.integration.webcert;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;

@Service
@RequiredArgsConstructor
public class ConvertCertificateService {

  public List<CertificateCategory> convert(
      List<List<CertificateDataElement>> certificateDataElements) {

    return certificateDataElements.stream()
        .map(this::toCertificateCategory)
        .collect(Collectors.toList());
  }

  private CertificateCategory toCertificateCategory(List<CertificateDataElement> elements) {
    return CertificateCategory.builder()
        .title(getCategoryTitle(elements.get(0)))
        .questions(
            elements.stream()
                .filter(removeCategory())
                .map(this::toCertificateQuestion)
                .collect(Collectors.toList())
        )
        .build();
  }

  private static Predicate<CertificateDataElement> removeCategory() {
    return element -> !element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }

  private CertificateQuestion toCertificateQuestion(CertificateDataElement element) {
    
    return null;
  }

  private String getCategoryTitle(CertificateDataElement element) {
    if (element.getConfig().getText() == null || element.getConfig().getText().isEmpty()) {
      throw new IllegalArgumentException("Category does not contain required field text");
    }
    return element.getConfig().getText();
  }
}
