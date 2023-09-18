package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class ListCertificatesService {

  private final GetCertificatesService getCertificatesService;
  private final UserService userService;

  public ListCertificatesResponse get(ListCertificatesRequest request) {
    if (userService.getLoggedInUser().isEmpty()) {
      throw new IllegalStateException();
    }

    // TODO: Add logging

    final var response = getCertificatesService.get(
        CertificatesRequest
            .builder()
            .patientId(userService.getLoggedInUser().get().getPersonId())
            .years(request.getYears())
            .units(request.getUnits())
            .statuses(request.getStatuses())
            .certificateTypes(request.getCertificateTypes())
            .build()
    );

    return ListCertificatesResponse.builder()
        .content(response.getContent())
        .build();

  }
}
