package se.inera.intyg.minaintyg.certificate.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.util.html.HTMLFactory;
import se.inera.intyg.minaintyg.util.html.HTMLTextFactory;
import se.inera.intyg.minaintyg.util.html.HTMLUtility;

@Service
@RequiredArgsConstructor
public class FormattedCategoryConverter {

  private final FormattedQuestionConverter formattedQuestionConverter;

  public String convert(CertificateCategory category) {
    return
        HTMLFactory.section(
            HTMLUtility.join(
                categoryTitle(category.getTitle()),
                category.getQuestions()
                    .stream()
                    .map(formattedQuestionConverter::convert)
                    .collect(Collectors.joining())
            )
        );
  }

  private String categoryTitle(String title) {
    return HTMLTextFactory.h2(title);
  }

}
