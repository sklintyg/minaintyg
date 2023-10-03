package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getTitleText;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;

@Service
@RequiredArgsConstructor
public class ConvertCertificateService {

  private final CategoryQuestionOrganizer categoryQuestionOrganizer;
  private final QuestionConverter questionConverter;

  public List<CertificateCategory> convert(CertificateDTO certificateDTO) {
    final var organizedByCategoryData = categoryQuestionOrganizer.organize(
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
                .map(questionConverter::convert)
                .toList()
        )
        .build();
  }
}
