package se.inera.intyg.minaintyg.integration.webcert;

import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getTitleText;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueBoolean;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueDateListSubQuestions;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueText;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

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
        .title(getTitleText(elements.get(0)))
        .questions(
            elements.stream()
                .filter(removeCategory())
                .map(this::toCertificateQuestion)
                .collect(Collectors.toList())
        )
        .build();
  }

  private CertificateQuestion toCertificateQuestion(CertificateDataElement element) {
    final var certificateQuestionBuilder = CertificateQuestion.builder();
    if (element.getValue() instanceof CertificateDataTextValue) {
      certificateQuestionBuilder.value(getValueText(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueBoolean) {
      certificateQuestionBuilder.value(getValueBoolean(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueDateList) {
      certificateQuestionBuilder.subQuestions(getValueDateListSubQuestions(element));
    }
    return certificateQuestionBuilder.title(getTitleText(element)).build();
  }

  private static Predicate<CertificateDataElement> removeCategory() {
    return element -> !element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }
}
