package se.inera.certificate.integration.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;

import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource implements IneraCertificateRestApi {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ModuleRestApiFactory moduleRestApiFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Response getCertificate(String certificateId) {
        Certificate certificate = certificateService.getCertificate(null, certificateId);
        if (certificate != null) {
            return Response.ok(certificate.getDocument()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getCertificatePdf(String certificateId) {

        Certificate certificate = certificateService.getCertificate(null, certificateId);

        if (certificate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ModuleRestApi moduleRestApi = moduleRestApiFactory.getModuleRestService(certificate.getType());

        try {
            Lakarutlatande lakarutlatande = objectMapper.readValue(certificate.getDocument(), Lakarutlatande.class);
            byte[] pdf = moduleRestApi.pdf(lakarutlatande);
            return Response.ok(pdf).build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to unmarshall lakarutlatande for certificate " + certificateId);
        }
    }
}
