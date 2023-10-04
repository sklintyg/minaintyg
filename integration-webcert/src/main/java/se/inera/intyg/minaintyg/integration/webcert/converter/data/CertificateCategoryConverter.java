package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;

@Service
@RequiredArgsConstructor
public class CertificateCategoryConverter {

  private final CertificateElementOrganizer certificateElementOrganizer;
  private final CertificateQuestionConverter certificateQuestionConverter;

  public List<CertificateCategory> convert(CertificateDTO certificateDTO) {
    final var organizedByCategoryData = certificateElementOrganizer.organize(
        getCertificateDataElements(certificateDTO)
    );

    return organizedByCategoryData.keySet().stream()
        .map(category -> toCertificateCategory(category, organizedByCategoryData.get(category)))
        .toList();
  }

  private static List<CertificateDataElement> getCertificateDataElements(
      CertificateDTO certificateDTO) {
    return certificateDTO.getData().values().stream().toList();
  }

  private CertificateCategory toCertificateCategory(CertificateDataElement category,
      List<CertificateDataElement> elements) {
    return CertificateCategory.builder()
        .title(getTitleText(category))
        .questions(
            elements.stream()
                .map(certificateQuestionConverter::convert)
                .toList()
        )
        .build();
  }

  public static String getTitleText(CertificateDataElement element) {
    if (element.getConfig().getText() == null || element.getConfig().getText().isEmpty()) {
      return null;
    }
    return element.getConfig().getText();
  }
}
