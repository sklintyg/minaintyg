package se.inera.intyg.minaintyg.integration.webcert.configuration;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;

@Component
public class EventConverter {

    //TODO update converter and tests after timestamp has been added to API
    public List<CertificateEvent> convert(CertificateMetadataDTO metadataDTO) {
        if (metadataDTO.isSent()) {
            return List.of(CertificateEvent.builder()
                .description(setDescription(metadataDTO))
                .build());
        }
        return Collections.emptyList();
    }

    private String setDescription(CertificateMetadataDTO metadataDTO) {
        if (metadataDTO.isSent()) {
            return "Skickat till " + metadataDTO.getSentTo();
        }
        return null;
    }

//    LocalDateTime timestamp;
//    String certificateId;
//    String description;
}
