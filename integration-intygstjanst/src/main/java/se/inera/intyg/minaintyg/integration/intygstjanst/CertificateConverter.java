package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateSummary;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateUnit;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;

@Service
@RequiredArgsConstructor
public class CertificateConverter {

  private final CertificateEventService certificateEventService;
  private final CertificateStatusService certificateStatusService;

  private static CertificateSummary convertSummary(CertificateDTO certificate) {
    return CertificateSummary
        .builder()
        .label(certificate.getSummary().getLabel())
        .value(certificate.getSummary().getValue())
        .build();
  }

  private static CertificateIssuer convertIssuer(CertificateDTO certificate) {
    return CertificateIssuer
        .builder()
        .name(certificate.getIssuer().getName())
        .build();
  }

  private static CertificateUnit convertUnit(CertificateDTO certificate) {
    return CertificateUnit
        .builder()
        .name(certificate.getUnit().getName())
        .id(certificate.getUnit().getId())
        .build();
  }

  private static CertificateType convertType(CertificateDTO certificate) {
    return CertificateType
        .builder()
        .name(certificate.getType().getName())
        .id(certificate.getType().getId())
        .version(certificate.getType().getVersion())
        .build();
  }

  public Certificate convert(CertificateDTO certificate) {
    return Certificate
        .builder()
        .id(certificate.getId())
        .type(convertType(certificate))
        .unit(convertUnit(certificate))
        .issuer(convertIssuer(certificate))
        .summary(convertSummary(certificate))
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
