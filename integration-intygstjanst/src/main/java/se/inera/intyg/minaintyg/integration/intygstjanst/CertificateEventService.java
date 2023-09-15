package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.List;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateEvent;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public interface CertificateEventService {

  List<CertificateEvent> get(List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient);
}
