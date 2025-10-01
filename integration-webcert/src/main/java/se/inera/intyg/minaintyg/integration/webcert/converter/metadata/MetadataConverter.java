package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit;

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
        .unit(convertUnit(metadataDTO.getUnit()))
        .careUnit(convertUnit(metadataDTO.getCareUnit()))
        .careProvider(convertUnit(metadataDTO.getCareProvider()))
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

  private CertificateUnit convertUnit(Unit unit) {
    return CertificateUnit.builder()
        .id(unit.getUnitId())
        .name(unit.getUnitName())
        .address(unit.getAddress())
        .zipCode(unit.getZipCode())
        .city(unit.getCity())
        .phoneNumber(unit.getPhoneNumber())
        .email(unit.getEmail())
        .build();
  }

  private CertificateRecipient convertRecipient(CertificateMetadataDTO metadataDTO) {
    if (recipientIsNull(metadataDTO) || certificateIsReplaced(metadataDTO)) {
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

  private boolean certificateIsReplaced(CertificateMetadataDTO metadataDTO) {
    return statusConverter.convert(metadataDTO).contains(CertificateStatusType.REPLACED);
  }
}