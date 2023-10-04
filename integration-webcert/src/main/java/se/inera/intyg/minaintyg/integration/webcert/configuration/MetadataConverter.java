package se.inera.intyg.minaintyg.integration.webcert.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;

@Component
@RequiredArgsConstructor
public class MetadataConverter {

    private final EventConverter eventConverter;

    public CertificateMetadata convert(CertificateMetadataDTO metadataDTO) {
        return CertificateMetadata.builder()
            .id(metadataDTO.getId())
            .type(convertType(metadataDTO))
            .issuer(convertIssuer(metadataDTO))
            .unit(convertUnit(metadataDTO))
            .events(eventConverter.convert(metadataDTO)) //TODO lägg till implementering för statuses
            .issued(metadataDTO.getCreated())
            .build();
    }

    private CertificateType convertType(CertificateMetadataDTO metadataDTO) {
        return CertificateType
            .builder()
            .id(metadataDTO.getType())
            .name(metadataDTO.getTypeName())
            .version(metadataDTO.getTypeVersion())
            .build();
    }

    private CertificateIssuer convertIssuer(CertificateMetadataDTO metadataDTO) {
        return CertificateIssuer
            .builder()
            .name(metadataDTO.getIssuedBy().getFullName())
            .build();
    }

    private CertificateUnit convertUnit(CertificateMetadataDTO metadataDTO) {
        return CertificateUnit
            .builder()
            .id(metadataDTO.getUnit().getUnitId())
            .name(metadataDTO.getUnit().getUnitName())
            .build();
    }

}
