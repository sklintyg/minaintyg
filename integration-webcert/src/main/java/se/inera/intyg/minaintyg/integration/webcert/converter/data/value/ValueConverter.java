package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

public interface ValueConverter {

  String NOT_PROVIDED = "Ej angivet";
  String TECHNICAL_ERROR = "Kan inte visa värdet pga tekniskt fel";
  CertificateQuestionValueText NOT_PROVIDED_VALUE = CertificateQuestionValueText.builder()
      .value(NOT_PROVIDED)
      .build();
  String MISSING_LABEL = "Saknas";
  
  CertificateDataValueType getType();

  CertificateQuestionValue convert(CertificateDataElement element);

  default boolean includeSubquestions() {
    return false;
  }

  default CertificateQuestionValue convert(CertificateDataElement element,
      List<CertificateDataElement> subQuestions) {
    return convert(element);
  }
}
