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

@Service
@RequiredArgsConstructor
public class GetCertificateFilterService {

  private final ListCertificatesService listCertificatesService;

  public GetCertificateFilterResponse get() {

    final var certificates = listCertificatesService.get(ListCertificatesRequest.builder().build());

    return GetCertificateFilterResponse
        .builder()
        .certificateTypes(getList(certificates, Certificate::getType))
        .years(getList(
                certificates,
                (certificate) -> String.valueOf(certificate.getIssued().getYear())
            )
        )
        .units(getList(certificates, Certificate::getUnit))
        .statuses(List.of(CertificateStatusType.SENT, CertificateStatusType.NOT_SENT))
        .build();
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
