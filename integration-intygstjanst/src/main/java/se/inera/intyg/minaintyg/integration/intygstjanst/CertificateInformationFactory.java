package se.inera.intyg.minaintyg.integration.intygstjanst;

import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;

public class CertificateInformationFactory {

  private CertificateInformationFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static CertificateSummary summary(CertificateDTO certificate) {
    return CertificateSummary
        .builder()
        .label(certificate.getSummary().getLabel())
        .value(certificate.getSummary().getValue())
        .build();
  }

  public static CertificateIssuer issuer(CertificateDTO certificate) {
    return CertificateIssuer
        .builder()
        .name(certificate.getIssuer().getName())
        .build();
  }

  public static CertificateUnit unit(CertificateDTO certificate) {
    return CertificateUnit
        .builder()
        .name(certificate.getUnit().getName())
        .id(certificate.getUnit().getId())
        .build();
  }

  public static CertificateType type(CertificateDTO certificate) {
    return CertificateType
        .builder()
        .name(certificate.getType().getName())
        .id(certificate.getType().getId())
        .version(certificate.getType().getVersion())
        .build();
  }

}
