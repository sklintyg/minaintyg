package se.inera.certificate.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.meta.ClinicalProcessCertificateMetaTypeBuilder;
import se.inera.certificate.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;
import se.inera.certificate.exception.ResultTypeErrorException;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.utils.ResultOfCallUtil;

import java.io.IOException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    private static final String ISSUER_NAME = "issuerName";
    private static final String FACILITY_NAME = "facilityName";
    private static final String TYPE = "type";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String AVAILABLE = "available";

    private static String document;

    @Mock
    private MessageSource messageSource = mock(MessageSource.class);

    @Mock
    private ListCertificatesForCitizenResponderInterface listServiceMock = mock(ListCertificatesForCitizenResponderInterface.class);

    @Mock
    private SendMedicalCertificateResponderInterface sendServiceMock = mock(SendMedicalCertificateResponderInterface.class);

    @Mock
    private GetCertificateContentResponderInterface getContentServiceMock = mock(GetCertificateContentResponderInterface.class);

    @InjectMocks
    private CertificateService service = new CertificateServiceImpl();

    private LocalDateTime signDateTime = new LocalDateTime();
    private LocalDate validFromDate = new LocalDate();
    private LocalDate validToDate = new LocalDate();
    private LocalDateTime firstTimeStamp = new LocalDateTime(2013, 1, 2, 20, 0);
    private LocalDateTime laterTimeStamp = new LocalDateTime(2013, 1, 3, 20, 0);

    private UtlatandeStatus unhandledStatus;
    private UtlatandeStatus deletedStatus;
    private UtlatandeStatus sentStatus;
    private UtlatandeStatus cancelledStatus;

    @BeforeClass
    public static void setupCertificateData() throws IOException {
        document = FileUtils.readFileToString(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile());
    }

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
        /* Given */
        ClinicalProcessCertificateMetaTypeBuilder builder = getClinicalProcessCertificateMetaTypeBuilder(
                unhandledStatus, deletedStatus, null, sentStatus);

        ListCertificatesForCitizenResponseType response  = new ListCertificatesForCitizenResponseType();
        response.getMeta().add(builder.build());
        response.setResult(ResultTypeUtil.okResult());

        /* When */
        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        /* Then */
        List<UtlatandeMetaData> certificates = service.getCertificates("19121212-1212");

        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getIssuerName().equals(ISSUER_NAME));
        assertTrue(certificates.get(0).getFacilityName().equals(FACILITY_NAME));
        assertTrue(certificates.get(0).getStatuses().get(0).getType().equals(StatusType.SENT));
    }
    
    @Test(expected=ResultTypeErrorException.class)
    public void testGetCertificatesFailureHandling() {
        ListCertificatesForCitizenResponseType response  = new ListCertificatesForCitizenResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "an error"));

        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates("19121212-1212");
    }

    @Test
    public void testGetCertificatesStatusOrder() {
        /* Given */
        cancelledStatus.setTimestamp(new LocalDateTime(2015, 1, 1, 12, 0));

        ClinicalProcessCertificateMetaTypeBuilder builder = getClinicalProcessCertificateMetaTypeBuilder(
                unhandledStatus, deletedStatus, cancelledStatus, sentStatus);

        ListCertificatesForCitizenResponseType response  = new ListCertificatesForCitizenResponseType();
        response.getMeta().add(builder.build());
        response.setResult(ResultTypeUtil.okResult());

        /* When */
        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        /* Then */
        List<UtlatandeMetaData> certificates = service.getCertificates("19121212-1212");

        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getStatuses().get(0).getType().equals(StatusType.CANCELLED));
    }

    @Test
    public void testSendCertificate() {
        /* Given */
        ClinicalProcessCertificateMetaTypeBuilder builder = getClinicalProcessCertificateMetaTypeBuilder(
                unhandledStatus, deletedStatus, null, sentStatus);

        ListCertificatesForCitizenResponseType listServiceResponse  = new ListCertificatesForCitizenResponseType();
        listServiceResponse.getMeta().add(builder.build());
        listServiceResponse.setResult(ResultTypeUtil.okResult());

        GetCertificateContentResponseType getContentServiceResponse = new GetCertificateContentResponseType();
        getContentServiceResponse.setResult(ResultOfCallUtil.okResult());
        getContentServiceResponse.setCertificate(document);

        SendMedicalCertificateResponseType sendServiceResponse = new SendMedicalCertificateResponseType();
        sendServiceResponse.setResult(ResultOfCallUtil.okResult());

        /* When */
        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(listServiceResponse);
        when(getContentServiceMock.getCertificateContent(any(AttributedURIType.class), any(GetCertificateContentRequestType.class))).thenReturn(getContentServiceResponse);
        when(sendServiceMock.sendMedicalCertificate(any(AttributedURIType.class), any(SendMedicalCertificateRequestType.class))).thenReturn(sendServiceResponse);
        //when(service.getUtlatande(any(String.class), any(String.class))).thenReturn(utlatandeWithMeta);

        /* Then */
        ModuleAPIResponse response = service.sendCertificate("19121212-1212", "12345678", "FK");

        assertEquals("sent", response.getResultCode());
    }

    private ClinicalProcessCertificateMetaTypeBuilder getClinicalProcessCertificateMetaTypeBuilder(
            UtlatandeStatus unhandled,
            UtlatandeStatus deleted,
            UtlatandeStatus cancelled,
            UtlatandeStatus sent
    ) {
        ClinicalProcessCertificateMetaTypeBuilder builder = new ClinicalProcessCertificateMetaTypeBuilder();
        builder.available(AVAILABLE)
                .certificateId(CERTIFIED_ID)
                .certificateType(TYPE)
                .facilityName(FACILITY_NAME)
                .issuerName(ISSUER_NAME)
                .signDate(signDateTime)
                .validity(validFromDate, validToDate);

        if (unhandled != null) { builder.status(unhandled); }
        if (deleted != null)   { builder.status(deleted); }
        if (cancelled != null) { builder.status(cancelled); }
        if (sent != null)      { builder.status(sent); }

        return builder;
    }

}
