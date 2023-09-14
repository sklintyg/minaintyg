package se.inera.intyg.minaintyg.certificate.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.*;
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
    public CertificateDTO convert(Certificate certificate) {
        return CertificateDTO
                .builder()
                .id(certificate.getId())
                .type(CertificateType
                        .builder()
                        .name(certificate.getType().getName())
                        .id(certificate.getType().getId())
                        .version(certificate.getType().getVersion())
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
