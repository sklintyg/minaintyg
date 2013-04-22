package se.inera.certificate.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCertificatesResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private ListCertificatesResponderInterface responder = new ListCertificatesResponderImpl();

    @Test
    public void listAnyCertificatesForUnknownPersonGivesEmptyList() {
        AttributedURIType logicalAddress = null;
        ListCertificatesRequestType parameters = new ListCertificatesRequestType();
        parameters.setNationalIdentityNumber("unknown-person");
        ListCertificatesResponseType result = responder.listCertificates(logicalAddress, parameters);
        assertTrue(result.getMeta().isEmpty());
    }

    @Test
    public void listCertificatesWithNoCertificates() {
        String civicRegistrationNumber = "19350108-1234";
        List<String> certificateTypes = Arrays.asList("fk7263");

        List<CertificateMetaData> result = Collections.emptyList();
        when(certificateService.listCertificates(civicRegistrationNumber, certificateTypes)).thenReturn(result);

        ListCertificatesRequestType parameters = createListCertificatesRequest(civicRegistrationNumber);

        ListCertificatesResponseType response = responder.listCertificates(null, parameters);

        assertEquals(0, response.getMeta().size());
        assertEquals(OK, response.getResult().getResultCode());
    }

    private ListCertificatesRequestType createListCertificatesRequest(String civicRegistrationNumber) {
        ListCertificatesRequestType parameters = new ListCertificatesRequestType();
        parameters.setNationalIdentityNumber(civicRegistrationNumber);
        return parameters;
    }
}
