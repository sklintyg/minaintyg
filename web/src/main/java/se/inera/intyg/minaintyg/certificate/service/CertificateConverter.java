package se.inera.intyg.minaintyg.certificate.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CompleteCertificate;
import se.inera.intyg.minaintyg.util.CertificateToHTMLFactory;

@Service
public class CertificateConverter {

  public FormattedCertificate convert(CompleteCertificate certificate) {
    return FormattedCertificate
        .builder()
        .metadata(certificate.getMetadata())
        .formattedContent(getFormattedContent(certificate))
        .build();
  }

  private String getFormattedContent(CompleteCertificate certificate) {
    return CertificateToHTMLFactory.certificate(getFormattedCategories(certificate));
  }

  private String getFormattedCategories(CompleteCertificate certificate) {
    return certificate.getCategories()
        .stream()
        .map(CertificateToHTMLFactory::category)
        .collect(Collectors.joining());
  }
}
