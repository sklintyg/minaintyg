package se.inera.certificate.web.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.meta.ClinicalProcessCertificateMetaTypeBuilder;
import se.inera.certificate.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.inera.certificate.exception.ResultTypeErrorException;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    private static final String ISSUER_NAME = "issuerName";

    private static final String FACILITY_NAME = "facilityName";

    private static final String TYPE = "type";

    private static final String CERTIFIED_ID = "certifiedId";

    private static final String AVAILABLE = "available";

    @Mock
    private MessageSource messageSource = mock(MessageSource.class);

    @Mock
    private ListCertificatesForCitizenResponderInterface listServiceMock = mock(ListCertificatesForCitizenResponderInterface.class);

    @InjectMocks
    private CertificateServiceImpl service = new CertificateServiceImpl();

    private LocalDateTime signDateTime = new LocalDateTime();
    private LocalDate validFromDate = new LocalDate();
    private LocalDate validToDate = new LocalDate();
    private LocalDateTime firstTimeStamp = new LocalDateTime(2013, 1, 2, 20, 0);
    private LocalDateTime laterTimeStamp = new LocalDateTime(2013, 1, 3, 20, 0);

    private UtlatandeStatus unhandledStatus;
    private UtlatandeStatus deletedStatus;
    private UtlatandeStatus sentStatus;
    private UtlatandeStatus cancelledStatus;

    @Before
    public void setup() {
        unhandledStatus = new UtlatandeStatus();
        unhandledStatus.setType(se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType.UNHANDLED);
        unhandledStatus.setTarget("FK");
        unhandledStatus.setTimestamp(firstTimeStamp);

        deletedStatus = new UtlatandeStatus();
        deletedStatus.setType(se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType.DELETED);
        deletedStatus.setTarget("FK");
        deletedStatus.setTimestamp(laterTimeStamp);

        sentStatus = new UtlatandeStatus();
        sentStatus.setType(se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType.SENT);
        sentStatus.setTarget("FK");
        sentStatus.setTimestamp(firstTimeStamp);

        cancelledStatus = new UtlatandeStatus();
        cancelledStatus.setType(se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType.CANCELLED);
        cancelledStatus.setTarget("FK");
        cancelledStatus.setTimestamp(firstTimeStamp);
    }

    @Test
    public void testGetCertificates() {
        ClinicalProcessCertificateMetaTypeBuilder builder = new ClinicalProcessCertificateMetaTypeBuilder();
        builder.available(AVAILABLE)
                .certificateId(CERTIFIED_ID)
                .certificateType(TYPE)
                .facilityName(FACILITY_NAME)
                .issuerName(ISSUER_NAME)
                .signDate(signDateTime)
                .validity(validFromDate, validToDate)
                .status(unhandledStatus)
                .status(deletedStatus)
                .status(sentStatus);

        ListCertificatesForCitizenResponseType response  = new ListCertificatesForCitizenResponseType();
        
        response.getMeta().add(builder.build());
        response.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(Mockito.any(String.class), Mockito.any(ListCertificatesForCitizenType.class))).thenReturn(response);
        List<UtlatandeMetaData> certificates = service.getCertificates("123456789");

        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getIssuerName().equals(ISSUER_NAME));
        assertTrue(certificates.get(0).getFacilityName().equals(FACILITY_NAME));
        assertTrue(certificates.get(0).getStatuses().get(0).getType().equals(StatusType.SENT));
    }
    
    @Test(expected=ResultTypeErrorException.class)
    public void testGetCertificatesFailureHandling() {
        ListCertificatesForCitizenResponseType response  = new ListCertificatesForCitizenResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "an error"));

        when(listServiceMock.listCertificatesForCitizen(Mockito.any(String.class), Mockito.any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates("123456789");
    }
    @Test
    public void testGetCertificatesStatusOrder() {
        UtlatandeStatus cancelledStatus = new UtlatandeStatus();
        cancelledStatus.setType(se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType.CANCELLED);
        cancelledStatus.setTarget("FK");
        cancelledStatus.setTimestamp(new LocalDateTime(2015, 1, 1, 12, 0));
        
        ClinicalProcessCertificateMetaTypeBuilder builder = new ClinicalProcessCertificateMetaTypeBuilder();
        builder.available(AVAILABLE)
                .certificateId(CERTIFIED_ID)
                .certificateType(TYPE)
                .facilityName(FACILITY_NAME)
                .issuerName(ISSUER_NAME)
                .signDate(signDateTime)
                .validity(validFromDate, validToDate)
                .status(unhandledStatus)
                .status(deletedStatus)
                .status(cancelledStatus)
                .status(unhandledStatus)
                .status(deletedStatus);

        ListCertificatesForCitizenResponseType response  = new ListCertificatesForCitizenResponseType();

        response.getMeta().add(builder.build());
        response.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(Mockito.any(String.class), Mockito.any(ListCertificatesForCitizenType.class))).thenReturn(response);
        List<UtlatandeMetaData> certificates = service.getCertificates("123456789");
        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getStatuses().get(0).getType().equals(StatusType.CANCELLED));
    }
}
