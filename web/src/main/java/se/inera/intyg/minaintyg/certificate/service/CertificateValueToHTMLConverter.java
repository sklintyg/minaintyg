package se.inera.intyg.minaintyg.certificate.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;

@Service
public class CertificateValueToHTMLConverter {

  String convert(CertificateCategory category) {
    return CertificateQuestionFactory.category(category);
  }
}
