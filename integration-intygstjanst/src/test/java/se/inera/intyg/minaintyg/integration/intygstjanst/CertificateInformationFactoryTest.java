package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateIssuerDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateSummaryDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateTypeDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateUnitDTO;

class CertificateInformationFactoryTest {

  private final static CertificateDTO CERTIFICATE = CertificateDTO
      .builder()
      .id("ID")
      .issuer(CertificateIssuerDTO.builder().name("issuerName").build())
      .type(CertificateTypeDTO.builder().id("typeId").name("typeName").version("version").build())
      .unit(CertificateUnitDTO.builder().id("unitId").name("unitName").build())
      .summary(CertificateSummaryDTO.builder().label("label").value("summary").build())
      .build();

  @Test
  void shouldCreateIssuer() {
    final var response = CertificateInformationFactory.issuer(CERTIFICATE);

    assertEquals(CERTIFICATE.getIssuer().getName(), response.getName());
  }

  @Test
  void shouldCreateTypeId() {
    final var response = CertificateInformationFactory.type(CERTIFICATE);

    assertEquals(CERTIFICATE.getType().getId(), response.getId());
  }

  @Test
  void shouldCreateTypeName() {
    final var response = CertificateInformationFactory.type(CERTIFICATE);

    assertEquals(CERTIFICATE.getType().getName(), response.getName());
  }

  @Test
  void shouldCreateTypeVersion() {
    final var response = CertificateInformationFactory.type(CERTIFICATE);

    assertEquals(CERTIFICATE.getType().getVersion(), response.getVersion());
  }

  @Test
  void shouldCreateSummaryLabel() {
    final var response = CertificateInformationFactory.summary(CERTIFICATE);

    assertEquals(CERTIFICATE.getSummary().getLabel(), response.getLabel());
  }

  @Test
  void shouldCreateSummaryValue() {
    final var response = CertificateInformationFactory.summary(CERTIFICATE);

    assertEquals(CERTIFICATE.getSummary().getValue(), response.getValue());
  }

  @Test
  void shouldCreateUnit() {
    final var response = CertificateInformationFactory.unit(CERTIFICATE);

    assertEquals(CERTIFICATE.getUnit().getId(), response.getId());
  }

  @Test
  void shouldCreateUnitName() {
    final var response = CertificateInformationFactory.unit(CERTIFICATE);

    assertEquals(CERTIFICATE.getUnit().getName(), response.getName());
  }
}