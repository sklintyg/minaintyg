package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

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

  CertificateDataValueType getType();

  CertificateQuestionValue convert(CertificateDataElement element);
}
