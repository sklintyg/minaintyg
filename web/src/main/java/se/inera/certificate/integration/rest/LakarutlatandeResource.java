package se.inera.certificate.integration.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.model.dao.CertificateStateHistoryEntry;
import se.inera.certificate.service.CertificateService;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource implements IneraCertificateRestApi {

    private static final Logger LOG = LoggerFactory.getLogger(LakarutlatandeResource.class);

    @Autowired
    private CertificateService certificateService;

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
            Lakarutlatande lakarutlatande = objectMapper.readValue(certificate.getDocument(), Lakarutlatande.class);
            convertStatus(certificate, lakarutlatande);
            return Response.ok(lakarutlatande).type(MediaType.APPLICATION_JSON).build();
        } catch (IOException e) {
            LOG.warn("Failed to unmarshal certificate '" + certificateId + "'.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static void convertStatus(Certificate source, Lakarutlatande target) {
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
    public Response sendCertificate(String civicRegistrationNumber, String certificateId, String target) {
        try {
            certificateService.sendCertificate(civicRegistrationNumber, certificateId, target);
            return Response.ok("{\"resultCode\": \"sent\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.ok("{\"resultCode\": \"error\"}").build();
        }
    }

}
