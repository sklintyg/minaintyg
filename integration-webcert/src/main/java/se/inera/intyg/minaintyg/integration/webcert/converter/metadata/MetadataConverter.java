package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;

@Component
@RequiredArgsConstructor
public class MetadataConverter {

  private final EventConverter eventConverter;
  private final StatusConverter statusConverter;

  public CertificateMetadata convert(CertificateMetadataDTO metadataDTO) {
    return CertificateMetadata.builder()
        .id(metadataDTO.getId())
        .type(convertType(metadataDTO))
        .issuer(convertIssuer(metadataDTO))
        .unit(convertUnit(metadataDTO))
        .events(eventConverter.convert(metadataDTO))
        .statuses(statusConverter.convert(metadataDTO))
        .issued(metadataDTO.getCreated())
        .recipient(convertRecipient(metadataDTO))
        .build();
  }

  private CertificateType convertType(CertificateMetadataDTO metadataDTO) {
    return CertificateType.builder()
        .id(metadataDTO.getType())
        .name(metadataDTO.getTypeName())
        .version(metadataDTO.getTypeVersion())
        .build();
  }

  private CertificateIssuer convertIssuer(CertificateMetadataDTO metadataDTO) {
    return CertificateIssuer.builder()
        .name(metadataDTO.getIssuedBy().getFullName())
        .build();
  }

  private CertificateUnit convertUnit(CertificateMetadataDTO metadataDTO) {
    return CertificateUnit.builder()
        .id(metadataDTO.getUnit().getUnitId())
        .name(metadataDTO.getUnit().getUnitName())
        .build();
  }

  private CertificateRecipient convertRecipient(CertificateMetadataDTO metadataDTO) {
    return CertificateRecipient.builder()
        .id(metadataDTO.getRecipient().getId())
        .name(metadataDTO.getRecipient().getName())
        .sent(metadataDTO.getRecipient().getSent())
        .build();
  }
}
