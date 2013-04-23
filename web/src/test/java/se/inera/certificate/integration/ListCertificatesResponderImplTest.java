package se.inera.certificate.integration;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
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
    public void listCertificatesWithNoCertificates() {
        String civicRegistrationNumber = "19350108-1234";
        List<String> certificateTypes = Collections.singletonList("fk7263");
        LocalDate fromDate = new LocalDate(2000, 1, 1);
        LocalDate toDate = new LocalDate(2020, 12, 12);

        List<CertificateMetaData> result = Collections.emptyList();

        when(certificateService.listCertificates(civicRegistrationNumber, certificateTypes, fromDate, toDate)).thenReturn(result);

        ListCertificatesRequestType parameters = createListCertificatesRequest(civicRegistrationNumber, certificateTypes, fromDate, toDate);

        ListCertificatesResponseType response = responder.listCertificates(null, parameters);

        verify(certificateService).listCertificates(civicRegistrationNumber, certificateTypes, fromDate, toDate);

        assertEquals(0, response.getMeta().size());
        assertEquals(OK, response.getResult().getResultCode());
    }

    private ListCertificatesRequestType createListCertificatesRequest(String civicRegistrationNumber, List<String> types, LocalDate fromDate, LocalDate toDate) {
        ListCertificatesRequestType parameters = new ListCertificatesRequestType();
        parameters.setNationalIdentityNumber(civicRegistrationNumber);
        for (String type: types) {
            parameters.getCertificateType().add(type);
        }

        parameters.setFromDate(fromDate);
        parameters.setToDate(toDate);

        return parameters;
    }
}
