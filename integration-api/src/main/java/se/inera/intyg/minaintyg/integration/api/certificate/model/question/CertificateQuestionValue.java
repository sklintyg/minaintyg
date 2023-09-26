package se.inera.intyg.minaintyg.integration.api.certificate.model.question;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class CertificateQuestionValue {

  CertificateQuestionValueType type;
}
