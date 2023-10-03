package se.inera.intyg.minaintyg.integration.intygstjanst;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;

@Service
public class CertificateRecipientConverter {

  public CertificateRecipient convert(CertificateRecipientDTO recipient) {
    return CertificateRecipient
        .builder()
        .id(recipient.getId())
        .name(recipient.getName())
        .sent(recipient.getSent())
        .build();
  }
}
