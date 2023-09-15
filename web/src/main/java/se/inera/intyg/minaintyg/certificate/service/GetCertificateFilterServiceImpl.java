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
public class GetCertificateFilterServiceImpl implements GetCertificateFilterService {

  private final ListCertificatesService listCertificatesService;

  private static <T> List<T> getList(ListCertificatesResponse certificates,
      Function<Certificate, T> getValueFunction) {
    return certificates
        .getContent()
        .stream()
        .map(getValueFunction)
        .toList();
  }

  @Override
  public GetCertificateFilterResponse get() {

    final var certificates = listCertificatesService.get(ListCertificatesRequest.builder().build());

    return GetCertificateFilterResponse
        .builder()
        .certificateTypes(
            getList(certificates, (certificate) -> certificate.getType().getId())
        )
        .years(
            getList(certificates, (certificate) -> certificate.getIssued().substring(0, 4))
        )
        .units(
            getList(certificates, Certificate::getUnit)
        )
        .statuses(
            List.of(CertificateStatusType.SENT, CertificateStatusType.NOT_SENT)
        )
        .build();
  }

}
