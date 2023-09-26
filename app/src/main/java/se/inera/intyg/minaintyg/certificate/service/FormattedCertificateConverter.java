package se.inera.intyg.minaintyg.certificate.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;

@Service
@RequiredArgsConstructor
public class FormattedCertificateConverter {

  private final FormattedCategoryConverter formattedCategoryConverter;

  public FormattedCertificate convert(Certificate certificate) {
    return FormattedCertificate
        .builder()
        .metadata(certificate.getMetadata())
        .formattedContent(
            certificate.getCategories()
                .stream()
                .map(formattedCategoryConverter::convert)
                .collect(Collectors.joining())
        )
        .build();
  }
}
