package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary;
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
        .careUnit(convertCareUnit(metadataDTO))
        .events(eventConverter.convert(metadataDTO))
        .statuses(statusConverter.convert(metadataDTO))
        .issued(metadataDTO.getCreated())
        .recipient(convertRecipient(metadataDTO))
        .summary(convertSummary(metadataDTO))
        .build();
  }

  private CertificateType convertType(CertificateMetadataDTO metadataDTO) {
    return CertificateType.builder()
        .id(metadataDTO.getType())
        .name(metadataDTO.getName())
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
        .address(metadataDTO.getUnit().getAddress())
        .zipCode(metadataDTO.getUnit().getZipCode())
        .city(metadataDTO.getUnit().getCity())
        .phoneNumber(metadataDTO.getUnit().getPhoneNumber())
        .email(metadataDTO.getUnit().getEmail())
        .build();
  }

  CertificateUnit convertCareUnit(CertificateMetadataDTO metadataDTO) {
    return CertificateUnit.builder()
        .id(metadataDTO.getCareUnit().getUnitId())
        .name(metadataDTO.getCareUnit().getUnitName())
        .address(metadataDTO.getCareUnit().getAddress())
        .zipCode(metadataDTO.getCareUnit().getZipCode())
        .city(metadataDTO.getCareUnit().getCity())
        .phoneNumber(metadataDTO.getCareUnit().getPhoneNumber())
        .email(metadataDTO.getCareUnit().getEmail())
        .build();
  }

  private CertificateRecipient convertRecipient(CertificateMetadataDTO metadataDTO) {
    if (recipientIsNull(metadataDTO)) {
      return null;
    }
    return CertificateRecipient.builder()
        .id(metadataDTO.getRecipient().getId())
        .name(metadataDTO.getRecipient().getName())
        .sent(metadataDTO.getRecipient().getSent())
        .build();
  }

  private CertificateSummary convertSummary(CertificateMetadataDTO metadataDTO) {
    if (summaryIsNull(metadataDTO)) {
      return CertificateSummary.builder().build();
    }

    return CertificateSummary.builder()
        .label(metadataDTO.getSummary().getLabel())
        .value(metadataDTO.getSummary().getValue())
        .build();
  }

  private static boolean summaryIsNull(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getSummary() == null;
  }

  private static boolean recipientIsNull(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getRecipient() == null;
  }
}