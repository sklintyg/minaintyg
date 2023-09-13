package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.Certificate;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.user.MinaIntygUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListCertificatesServiceImpl implements ListCertificatesService {

    private final GetCertificatesService getCertificatesService;
    private final MinaIntygUserService userService;
    private final CertificateConverter certificateConverter;

    @Override
    public List<Certificate> get(ListCertificatesRequest request) {
       final var response = getCertificatesService.get(
               se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest
                       .builder()
                       .patientId(userService.getUser().getPersonId())
                       .years(request.getYears())
                       .units(request.getUnits())
                       .certificateTypes(request.getCertificateTypes())
                       .build()
        );

       return response
               .getContent()
               .stream()
               .map(certificateConverter::convert)
               .collect(Collectors.toList());

    }
}
