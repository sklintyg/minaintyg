package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificate")
public class CertificateController {

    private final ListCertificatesService listCertificatesService;

    @PostMapping("")
    public CertificatesResponse listCertificates(CertificatesRequest request) {
        final var listCertificatesRequest =
                ListCertificatesRequest
                    .builder()
                    .years(request.getYears())
                    .certificateTypes(request.getCertificateTypes())
                    .units(request.getUnits())
                    .statuses(request.getStatuses())
                    .build();

        return CertificatesResponse
                .builder()
                .content(listCertificatesService.get(listCertificatesRequest).getContent())
                .build();
    }

}
