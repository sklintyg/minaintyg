package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public interface CertificateStatusService {

  List<CertificateStatusType> get(List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient,
      LocalDateTime issued);

}
