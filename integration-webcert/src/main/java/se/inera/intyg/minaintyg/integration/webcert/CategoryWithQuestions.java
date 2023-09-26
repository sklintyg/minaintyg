package se.inera.intyg.minaintyg.integration.webcert;

import java.util.ArrayList;
import java.util.List;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;

@Value
public class CategoryWithQuestions {

  CertificateDataElement category;
  List<CertificateDataElement> questions;

  public CategoryWithQuestions(CertificateDataElement category) {
    this.category = category;
    this.questions = new ArrayList<>();
  }

  public void addQuestion(CertificateDataElement question) {
    this.questions.add(question);
  }
}
