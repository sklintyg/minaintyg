package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateListItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateListRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateListResponse;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

@Service
@RequiredArgsConstructor
public class IntygstjanstCertificateIntegrationService implements GetCertificatesService {

  private final GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;
  private final CertificateConverter certificateConverter;

  @Override
  public CertificateListResponse get(CertificateListRequest request) {
    validateRequest(request);
    try {
      final var response = getCertificatesFromIntygstjanstService.get(request);
      return CertificateListResponse
          .builder()
          .content(convertContent(response))
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private List<CertificateListItem> convertContent(CertificatesResponseDTO response) {
    return response.getContent()
        .stream()
        .map(certificateConverter::convert)
        .toList();
  }

  private void validateRequest(CertificateListRequest request) {
    if (request == null || request.getPatientId() == null || request.getPatientId().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided, must contain patient id");
    }
  }
}