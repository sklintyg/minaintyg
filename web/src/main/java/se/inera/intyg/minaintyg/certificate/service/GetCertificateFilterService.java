package se.inera.intyg.minaintyg.certificate.service;

import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateFilterResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateUnit;

@Service
@RequiredArgsConstructor
public class GetCertificateFilterService {

  private final ListCertificatesService listCertificatesService;

  public GetCertificateFilterResponse get() {

    final var certificates = listCertificatesService.get(ListCertificatesRequest.builder().build());

    return GetCertificateFilterResponse
        .builder()
        .certificateTypes(getCertificateTypes(certificates))
        .years(getYears(certificates))
        .units(getUnits(certificates))
        .statuses(getStatuses())
        .build();
  }

  private List<String> getYears(ListCertificatesResponse certificates) {
    return getList(
        certificates,
        (certificate) -> String.valueOf(certificate.getIssued().getYear())
    );
  }

  private List<CertificateType> getCertificateTypes(ListCertificatesResponse certificates) {
    return getList(certificates, Certificate::getType);
  }

  private List<CertificateUnit> getUnits(ListCertificatesResponse certificates) {
    return getList(certificates, Certificate::getUnit);
  }

  private List<CertificateStatusType> getStatuses() {
    return List.of(CertificateStatusType.SENT, CertificateStatusType.NOT_SENT);
  }

  private <T> List<T> getList(ListCertificatesResponse certificates,
      Function<Certificate, T> getValueFunction) {
    return certificates
        .getContent()
        .stream()
        .map(getValueFunction)
        .distinct()
        .toList();
  }
}
