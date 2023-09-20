package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;

@Service
@RequiredArgsConstructor
public class CertificateConverter {

  private final CertificateEventService certificateEventService;
  private final CertificateStatusService certificateStatusService;

  public Certificate convert(CertificateDTO certificate) {
    return Certificate
        .builder()
        .id(certificate.getId())
        .type(CertificateInformationFactory.type(certificate))
        .unit(CertificateInformationFactory.unit(certificate))
        .issuer(CertificateInformationFactory.issuer(certificate))
        .summary(CertificateInformationFactory.summary(certificate))
        .issued(certificate.getIssued())
        .statuses(convertStatuses(certificate))
        .events(convertEvents(certificate))
        .build();
  }

  private List<CertificateEvent> convertEvents(CertificateDTO certificate) {
    return certificateEventService.get(certificate.getRelations(), certificate.getRecipient());
  }

  private List<CertificateStatusType> convertStatuses(CertificateDTO certificate) {
    return certificateStatusService.get(
        certificate.getRelations(),
        certificate.getRecipient(),
        certificate.getIssued()
    );
  }
}
