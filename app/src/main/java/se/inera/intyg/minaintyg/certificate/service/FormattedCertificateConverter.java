package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.util.html.HTMLFactory;
import se.inera.intyg.minaintyg.util.html.HTMLUtility;

@Service
@RequiredArgsConstructor
public class FormattedCertificateConverter {

  private final FormattedCategoryConverter formattedCategoryConverter;

  public FormattedCertificate convert(Certificate certificate) {
    return FormattedCertificate
        .builder()
        .metadata(certificate.getMetadata())
        .formattedContent(
            HTMLFactory.article(
                HTMLUtility.fromList(
                    certificate.getCategories(),
                    formattedCategoryConverter::convert
                )
            )
        )
        .build();
  }
}
