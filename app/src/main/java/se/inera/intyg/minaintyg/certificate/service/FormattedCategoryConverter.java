package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.util.html.HTMLUtility;

@Service
@RequiredArgsConstructor
public class FormattedCategoryConverter {

  private final FormattedQuestionConverter formattedQuestionConverter;

  public FormattedCertificateCategory convert(CertificateCategory category) {
    return FormattedCertificateCategory
        .builder()
        .heading(category.getTitle())
        .body(
            HTMLUtility.fromList(
                category.getQuestions(),
                formattedQuestionConverter::convert
            )
        )
        .build();
  }
}
