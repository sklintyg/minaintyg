package se.inera.intyg.minaintyg.certificate.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.util.html.CertificateToHTMLFactory;

@Service
public class CertificateToFormattedCertificateConverter {

  public FormattedCertificate convert(Certificate certificate) {
    return FormattedCertificate
        .builder()
        .metadata(certificate.getMetadata())
        .formattedContent(getFormattedContent(certificate))
        .build();
  }

  private String getFormattedContent(Certificate certificate) {
    return CertificateToHTMLFactory.certificate(getFormattedCategories(certificate));
  }

  private String getFormattedCategories(Certificate certificate) {
    return certificate.getCategories()
        .stream()
        .map(CertificateToHTMLFactory::category)
        .collect(Collectors.joining());
  }
}
