package se.inera.intyg.minaintyg.integration.intygstjanst;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesResponse;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstService;

@Service
@RequiredArgsConstructor
public class IntygstjanstCertificateIntegrationService implements GetCertificatesService {

  private final GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;
  private final CertificateConverter certificateConverter;

  @Override
  public CertificatesResponse get(CertificatesRequest request) {
    validateRequest(request);
    try {
      final var response = getCertificatesFromIntygstjanstService.get(request);
      return CertificatesResponse
          .builder()
          .content(
              response.getContent()
                  .stream()
                  .map(certificateConverter::convert)
                  .toList()
          )
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private void validateRequest(CertificatesRequest request) {
    if (request == null || request.getPatientId() == null || request.getPatientId().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided: " + request);
    }
  }
}