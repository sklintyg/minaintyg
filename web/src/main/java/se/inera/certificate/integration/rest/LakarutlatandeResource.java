package se.inera.certificate.integration.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.model.dao.CertificateStateHistoryEntry;
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
    public Response getCertificate(String civicRegistrationNumber, String certificateId) {

        Certificate certificate;
        try {
            certificate = certificateService.getCertificate(civicRegistrationNumber, certificateId);
        } catch (MissingConsentException ex) {
            LOG.warn("Tried to fetch certificate '" + certificateId + "' for patient '" + civicRegistrationNumber + "' without consent.", ex);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (certificate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            Utlatande utlatande = objectMapper.readValue(certificate.getDocument(), Utlatande.class);
            convertStatus(certificate, utlatande);
            return Response.ok(utlatande).type(MediaType.APPLICATION_JSON).build();
        } catch (IOException e) {
            LOG.warn("Failed to unmarshal certificate '" + certificateId + "'.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static void convertStatus(Certificate source, Utlatande target) {
        List<Status> statusList = new ArrayList<>();
        for (CertificateStateHistoryEntry historyEntry : source.getStates()) {
            Status status = new Status();
            status.setTarget(historyEntry.getTarget());
            status.setTimestamp(historyEntry.getTimestamp());
            status.setType(historyEntry.getState());
            statusList.add(status);
        }
        target.setStatus(statusList);
    }

    @Override
    public Response getCertificatePdf(String civicRegistrationNumber, String certificateId) {

        Certificate certificate;
        try {
            certificate = certificateService.getCertificate(civicRegistrationNumber, certificateId);
        } catch (MissingConsentException ex) {
            LOG.warn("Tried to get certificate '" + certificateId + "' as PDF for patient '" + civicRegistrationNumber + "' without consent.", ex);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (certificate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Utlatande utlatande;
        try {
            // unmarshal the certificate
            utlatande = objectMapper.readValue(certificate.getDocument(), Utlatande.class);
        } catch (IOException e) {
            LOG.error("Failed to unmarshall lakarutlatande for certificate " + certificateId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        // delegate the PDF generation to the certificate module
        ModuleRestApi moduleRestApi = moduleRestApiFactory.getModuleRestService(certificate.getType());
        Response response = moduleRestApi.pdf(utlatande);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            LOG.error("Failed to create PDF for certificate #" + certificateId + ". Certificate module returned status code " + response.getStatus());
            return Response.status(response.getStatus()).build();
        }

        return Response.ok(response.getEntity()).header(CONTENT_DISPOSITION, "attachment; filename=" + pdfFileName(utlatande)).build();

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

    private String pdfFileName(Utlatande utlatande) {
        return String.format("lakarutlatande_%s_%s-%s.pdf",
                utlatande.getPatient().getId(),
                utlatande.getValidFromDate().toString(DATE_FORMAT),
                utlatande.getValidToDate().toString(DATE_FORMAT));
    }
}
