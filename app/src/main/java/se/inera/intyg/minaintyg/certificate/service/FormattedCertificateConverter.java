package se.inera.intyg.minaintyg.certificate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;

@Service
@RequiredArgsConstructor
public class FormattedCertificateConverter {

  private final FormattedCategoryConverter formattedCategoryConverter;

  public FormattedCertificate convert(Certificate certificate) {
    return FormattedCertificate
        .builder()
        .metadata(certificate.getMetadata())
        .content(convertCategories(certificate.getCategories()))
        .build();
  }

  private List<FormattedCertificateCategory> convertCategories(
      List<CertificateCategory> categories) {
    return categories
        .stream()
        .map(formattedCategoryConverter::convert)
        .toList();
  }
}
