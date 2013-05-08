package se.inera.certificate.web.service;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.api.CertificateMeta;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {
    
    private static final String ISSUER_NAME = "issuerName";

    private static final String FACILITY_NAME = "facilityName";

    private static final String TYPE = "type";

    private static final String CERTIFIED_ID = "certifiedId";

    private static final String AVAILABLE = "available";

    @Mock
    MessageSource messageSource = mock(MessageSource.class);
    
    @Mock
    ListCertificatesResponderInterface listServiceMock = mock(ListCertificatesResponderInterface.class);

    @InjectMocks
    private CertificateService service = new CertificateServiceImpl();

    private LocalDate signDate = new LocalDate();
    private LocalDate validFromDate = new LocalDate();
    private LocalDate validToDate = new LocalDate();
    private LocalDateTime firstTimeStamp = new LocalDateTime(2013, 1, 2, 20, 0);
    private LocalDateTime laterTimeStamp = new LocalDateTime(2013, 1, 3, 20, 0);
    
    private CertificateStatusType unhandledStatus;
    
    private CertificateStatusType deletedStatus;
    private CertificateStatusType sentStatus;
    
    @Before 
    public void setup() {
        unhandledStatus = new CertificateStatusType();
        unhandledStatus.setType(StatusType.UNHANDLED);
        unhandledStatus.setTarget("FK");
        unhandledStatus.setTimestamp(firstTimeStamp);
        
        deletedStatus = new CertificateStatusType();
        deletedStatus.setType(StatusType.DELETED);
        deletedStatus.setTarget("FK");
        deletedStatus.setTimestamp(laterTimeStamp);
        
        sentStatus = new CertificateStatusType();
        sentStatus.setType(StatusType.SENT);
        sentStatus.setTarget("FK");
        sentStatus.setTimestamp(firstTimeStamp);
        
    }
    
    @Test
    public void testConversion() {
        CertificateMetaType meta = new CertificateMetaType();
        meta.setAvailable(AVAILABLE);
        meta.setCertificateId(CERTIFIED_ID);
        meta.setCertificateType(TYPE);
        meta.setFacilityName(FACILITY_NAME);
        meta.setIssuerName(ISSUER_NAME);
        meta.setSignDate(signDate);
        meta.setValidFrom(validFromDate);
        meta.setValidTo(validToDate);
        meta.getStatus().add(unhandledStatus);
        meta.getStatus().add(deletedStatus);
        meta.getStatus().add(sentStatus);
        
        List<CertificateMetaType> responseList  = new ArrayList<CertificateMetaType>();
        responseList.add(meta);
        
        ListCertificatesResponseType responseMock = mock(ListCertificatesResponseType.class);
        when(responseMock.getMeta()).thenReturn(responseList);
        
        when(listServiceMock.listCertificates(Mockito.any(AttributedURIType.class), Mockito.any(ListCertificatesRequestType.class))).thenReturn(responseMock);
        List<CertificateMeta> certificates = service.getCertificates("123456789");
        
        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getCaregiverName().equals(ISSUER_NAME));
        assertTrue(certificates.get(0).getCareunitName() .equals(FACILITY_NAME));
        assertTrue(certificates.get(0).getStatus().equals(StatusType.SENT.toString()));
    }

   
    
    @Test
    public void testLastestRResurnedStatusIsSENT() {
        CertificateMetaType meta = new CertificateMetaType();
        meta.setAvailable(AVAILABLE);
        meta.setCertificateId(CERTIFIED_ID);
        meta.setCertificateType(TYPE);
        meta.setFacilityName(FACILITY_NAME);
        meta.setIssuerName(ISSUER_NAME);
        meta.setSignDate(signDate);
        meta.setValidFrom(validFromDate);
        meta.setValidTo(validToDate);
        CertificateStatusType cancelledStatus = new CertificateStatusType();
        cancelledStatus.setType(StatusType.CANCELLED);
        cancelledStatus.setTimestamp(new LocalDateTime(2015,1,1,12,0));
        meta.getStatus().add(unhandledStatus);
        meta.getStatus().add(deletedStatus);
        meta.getStatus().add(cancelledStatus); //the one
        meta.getStatus().add(unhandledStatus);
        meta.getStatus().add(deletedStatus);
        
        List<CertificateMetaType> responseList  = new ArrayList<CertificateMetaType>();
        responseList.add(meta);
        
        ListCertificatesResponseType responseMock = mock(ListCertificatesResponseType.class);
        when(responseMock.getMeta()).thenReturn(responseList);
        
        when(listServiceMock.listCertificates(Mockito.any(AttributedURIType.class), Mockito.any(ListCertificatesRequestType.class))).thenReturn(responseMock);
        List<CertificateMeta> certificates = service.getCertificates("123456789");
        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getStatus().equals(StatusType.CANCELLED.toString()));
    }


}
