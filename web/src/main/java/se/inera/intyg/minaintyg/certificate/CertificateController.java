package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.dto.Certificate;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificate")
public class CertificateController {

    private final ListCertificatesService listCertificatesService;

    @PostMapping("")
    public List<Certificate> listCertificates(CertificatesRequest request) {
        return listCertificatesService.get(
                ListCertificatesRequest
                        .builder()
                        .years(request.getYears())
                        .certificateTypes(request.getCertificateTypes())
                        .units(request.getUnits())
                        .statuses(request.getStatuses())
                        .build()
        );
    }

}
