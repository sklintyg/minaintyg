package se.inera.certificate.integration.rest;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource implements IneraCertificateRestApi {

    private static final Logger LOG = LoggerFactory.getLogger(LakarutlatandeResource.class);
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String DATE_FORMAT = "yyyyMMdd";

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
            Response response = moduleRestApi.pdf(lakarutlatande);
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOG.error("Failed to create PDF for certificate #" + certificateId + ". Certificate module returned status code " + response.getStatus());
                return Response.status(response.getStatus()).build();
            }
            return Response.ok(response.getEntity()).header(CONTENT_DISPOSITION, "attachment; filename=" + pdfFileName(lakarutlatande)).build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to unmarshall lakarutlatande for certificate " + certificateId, e);
        }
    }

    @Override
    public Response sendCertificate(String civicRegistrationNumber, String certificateId, String target) {
        try {
            certificateService.sendCertificate(civicRegistrationNumber, certificateId, target);
            return Response.ok("{\"resultCode\": \"sent\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.ok("{\"resultCode\": \"error\"}").build();
        }
    }

    private String pdfFileName(Lakarutlatande lakarutlatande) {
        return String.format("lakarutlatande_%s_%s-%s.pdf",
                lakarutlatande.getPatient().getId(),
                lakarutlatande.getValidFromDate().toString(DATE_FORMAT),
                lakarutlatande.getValidToDate().toString(DATE_FORMAT));
    }
}
