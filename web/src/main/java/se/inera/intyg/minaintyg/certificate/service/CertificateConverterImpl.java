package se.inera.intyg.minaintyg.certificate.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateIssuer;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateSummary;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateType;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateUnit;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate;

@Service
public class CertificateConverterImpl implements CertificateConverter {

    private final CertificateEventService certificateEventService;
    private final CertificateStatusService certificateStatusService;

    public CertificateConverterImpl(CertificateEventService certificateEventService,
                                    CertificateStatusService certificateStatusService) {
        this.certificateEventService = certificateEventService;
        this.certificateStatusService = certificateStatusService;
    }

    @Override
    public se.inera.intyg.minaintyg.certificate.service.dto.Certificate convert(Certificate certificate) {
        return se.inera.intyg.minaintyg.certificate.service.dto.Certificate
                .builder()
                .id(certificate.getId())
                .type(CertificateType
                        .builder()
                        .name(certificate.getType().getName())
                        .id(certificate.getType().getId())
                        .build())
                .unit(CertificateUnit
                                .builder()
                                .name(certificate.getUnit().getName())
                                .id(certificate.getUnit().getId())
                                .build())
                .issuer(
                        CertificateIssuer
                                .builder()
                                .name(certificate.getIssuer().getName())
                                .build()
                )
                .summary(
                        CertificateSummary
                                .builder()
                                .label(certificate.getSummary().getLabel())
                                .value(certificate.getSummary().getValue())
                                .build()
                )
                .issued(certificate.getIssued().toString())
                .statuses(certificateStatusService.all(
                            certificate.getRelations(),
                            certificate.getRecipient(),
                            certificate.getIssued()
                        )
                )
                .events(certificateEventService.get(certificate.getRelations(), certificate.getRecipient())
                )
                .build();
    }
}
