package se.inera.intyg.minaintyg.integration.webcert.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

@Component
public class EventConverter {

    public static final String EVENT_SENT_TO_DESCRIPTION = "Skickat till ";
    public static final String EVENT_REPLACED = "Ersatt av vården med ett nytt intyg";
    public static final String EVENT_REPLACES = "Ersätter ett intyg som inte längre är aktuellt";

    //TODO update converter and tests after timestamp has been added to API
    public List<CertificateEvent> convert(CertificateMetadataDTO metadataDTO) {
        final var events = new ArrayList<CertificateEvent>();

        if (metadataDTO.isSent()) {
            events.add(CertificateEvent.builder()
                .description(EVENT_SENT_TO_DESCRIPTION + metadataDTO.getSentTo())
                .build());
        }

        events.addAll(convertReplacingCertificate(metadataDTO));
        events.addAll(convertReplacedCertificate(metadataDTO));

        return events;
    }

    private List<CertificateEvent> convertReplacedCertificate(CertificateMetadataDTO metadataDTO) {
        if (metadataDTO.getRelations() == null || metadataDTO.getRelations().getChildren() == null || replacingCertificateIsRevoked(
            metadataDTO)) {
            return Collections.emptyList();
        }

        return Arrays.stream(metadataDTO
                .getRelations()
                .getChildren())
            .filter(child -> child.getType() == CertificateRelationType.REPLACED || child.getType() == CertificateRelationType.COMPLEMENTED)
            .map(relation -> CertificateEvent.builder()
                .certificateId(relation.getCertificateId())
                .description(EVENT_REPLACED)
                .timestamp(relation.getCreated())
                .build()).toList();
    }

    private List<CertificateEvent> convertReplacingCertificate(CertificateMetadataDTO metadataDTO) {
        if (metadataDTO.getRelations() == null || metadataDTO.getRelations().getParent() == null) {
            return Collections.emptyList();
        }

        if (CertificateRelationType.REPLACED.equals(metadataDTO.getRelations().getParent().getType())
            || CertificateRelationType.COMPLEMENTED.equals(metadataDTO.getRelations().getParent().getType())) {
            return List.of(
                CertificateEvent.builder()
                    .certificateId(metadataDTO.getRelations().getParent().getCertificateId())
                    .timestamp(metadataDTO.getRelations().getParent().getCreated())
                    .description(EVENT_REPLACES)
                    .build()
            );
        }
        return Collections.emptyList();
    }

    private boolean replacingCertificateIsRevoked(CertificateMetadataDTO metadataDTO) {
        final var childStatus = Arrays.stream(metadataDTO
                .getRelations()
                .getChildren())
            .filter(child -> child.getStatus() == CertificateStatus.REVOKED);

        return childStatus.findAny().isPresent();

    }
}
