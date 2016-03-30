/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.minaintyg.web.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapFault;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import se.inera.intyg.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.*;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.builder.ClinicalProcessCertificateMetaTypeBuilder;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.insuranceprocess.healthreporting.getcertificatecontent.rivtabp20.v1.GetCertificateContentResponderInterface;
import se.inera.intyg.minaintyg.web.api.ModuleAPIResponse;
import se.inera.intyg.minaintyg.web.exception.ResultTypeErrorException;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeWithMeta;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    private static final String ISSUER_NAME = "issuerName";
    private static final String FACILITY_NAME = "facilityName";
    private static final String TYPE = "type";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String AVAILABLE = "available";

    @Mock
    private MessageSource messageSource;

    @Mock
    private ListCertificatesForCitizenResponderInterface listServiceMock;

    @Mock
    private SendCertificateToRecipientResponderInterface sendServiceMock;

    @Mock
    private GetCertificateContentResponderInterface getContentServiceMock;

    @Mock
    private MonitoringLogService monitoringServiceMock;

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @Mock
    private GetCertificateResponderInterface getCertificateService;

    @Mock
    private CitizenService citizenService;

    @InjectMocks
    private CertificateServiceImpl service;

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
    public void inject_fields() {
        service.setVardReferensId("MI");
        service.setLogicalAddress("FKORG");
    }

    @Before
    public void inject_status() {
        unhandledStatus = new UtlatandeStatus();
        unhandledStatus.setType(se.riv.clinicalprocess.healthcond.certificate.v1.StatusType.UNHANDLED);
        unhandledStatus.setTarget("FK");
        unhandledStatus.setTimestamp(firstTimeStamp);

        deletedStatus = new UtlatandeStatus();
        deletedStatus.setType(se.riv.clinicalprocess.healthcond.certificate.v1.StatusType.DELETED);
        deletedStatus.setTarget("FK");
        deletedStatus.setTimestamp(laterTimeStamp);

        sentStatus = new UtlatandeStatus();
        sentStatus.setType(se.riv.clinicalprocess.healthcond.certificate.v1.StatusType.SENT);
        sentStatus.setTarget("FK");
        sentStatus.setTimestamp(firstTimeStamp);

        cancelledStatus = new UtlatandeStatus();
        cancelledStatus.setType(se.riv.clinicalprocess.healthcond.certificate.v1.StatusType.CANCELLED);
        cancelledStatus.setTarget("FK");
        cancelledStatus.setTimestamp(firstTimeStamp);
    }

    @Before
    public void mockObjectMapper() {
        CertificateServiceImpl.objectMapper = mock(CustomObjectMapper.class);
    }

    @Test
    public void testGetUtlatande() throws Exception {
        final String document = "document";
        final String id = "lisu";
        final String part = "part";

        when(moduleRegistry.moduleExists(id)).thenReturn(true);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getUtlatandeFromIntyg(any())).thenReturn(mock(Utlatande.class));
        when(moduleRegistry.getModuleApi(id)).thenReturn(api);
        when(getCertificateService.getCertificate(anyString(), any())).thenReturn(createGetCertificateResponseType(part));
        when(CertificateServiceImpl.objectMapper.writeValueAsString(any())).thenReturn(document);

        Optional<UtlatandeWithMeta> res = service.getUtlatande(id, new Personnummer("19121212-1212"), CERTIFIED_ID);

        assertNotNull(res);
        assertTrue(res.isPresent());
        // We sort the result for testing purposes because the order which the statuses comes in does not matter
        Collections.sort(res.get().getStatuses(), (a, b) -> a.getType().name().compareTo(b.getType().name()));

        assertEquals(1, res.get().getStatuses().size());
        assertEquals(CertificateState.SENT, res.get().getStatuses().get(0).getType());
        assertEquals(part, res.get().getStatuses().get(0).getTarget());
        assertEquals(document, res.get().getDocument());

        verify(getCertificateService, times(1)).getCertificate(anyString(), any());
    }

    private GetCertificateResponseType createGetCertificateResponseType(final String part) {
        GetCertificateResponseType response = new GetCertificateResponseType();
        Intyg intyg = new Intyg();

        intyg.getStatus().add(createStatus(StatusKod.SENTTO.name(), part));
        intyg.getStatus().add(createStatus(StatusKod.RECEIV.name(), part));

        TypAvIntyg typ = new TypAvIntyg();
        typ.setCode("lisu");
        intyg.setTyp(typ);
        response.setIntyg(intyg);
        return response;
    }

    private IntygsStatus createStatus(String statuskod, String partkod) {
        IntygsStatus intygsStatus = new IntygsStatus();
        Statuskod sk = new Statuskod();
        sk.setCode(statuskod);
        intygsStatus.setStatus(sk);
        Part part = new Part();
        part.setCode(partkod);
        intygsStatus.setPart(part);
        return intygsStatus;
    }

    @Test
    public void testGetCertificates() {
        /* Given */
        ClinicalProcessCertificateMetaTypeBuilder builder = getClinicalProcessCertificateMetaTypeBuilder(
                unhandledStatus, deletedStatus, null, sentStatus);

        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.getMeta().add(builder.build());
        response.setResult(ResultTypeUtil.okResult());

        /* When */
        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        /* Then */
        List<UtlatandeMetaData> certificates = service.getCertificates(new Personnummer("19121212-1212"));

        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getIssuerName().equals(ISSUER_NAME));
        assertTrue(certificates.get(0).getFacilityName().equals(FACILITY_NAME));
        assertTrue(certificates.get(0).getStatuses().get(0).getType().equals(CertificateState.SENT));
    }

    @Test(expected = ResultTypeErrorException.class)
    public void testGetCertificatesFailureHandling() {
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "an error"));

        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates(new Personnummer("19121212-1212"));
    }

    @Test
    public void testGetCertificatesStatusOrder() {
        /* Given */
        cancelledStatus.setTimestamp(new LocalDateTime(2015, 1, 1, 12, 0));

        ClinicalProcessCertificateMetaTypeBuilder builder = getClinicalProcessCertificateMetaTypeBuilder(
                unhandledStatus, deletedStatus, cancelledStatus, sentStatus);

        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.getMeta().add(builder.build());
        response.setResult(ResultTypeUtil.okResult());

        /* When */
        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        /* Then */
        List<UtlatandeMetaData> certificates = service.getCertificates(new Personnummer("19121212-1212"));

        assertTrue(certificates.size() == 1);
        assertTrue(certificates.get(0).getStatuses().get(0).getType().equals(CertificateState.CANCELLED));
    }

    @Test
    public void testSendCertificate() {

        String personId = "19121212-1212";
        String citizenUsername = "19121212-1212";
        String utlatandeId = "1234567890";
        String mottagare = "FK";

        SendCertificateToRecipientResponseType response = new SendCertificateToRecipientResponseType();
        response.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());
        Citizen citizen = mock(Citizen.class);

        /* When */
        when(sendServiceMock.sendCertificateToRecipient(any(String.class), any(SendCertificateToRecipientType.class))).thenReturn(response);
        when(citizen.getUsername()).thenReturn(citizenUsername);
        when(citizenService.getCitizen()).thenReturn(citizen);

        /* Then */
        ModuleAPIResponse apiResponse = service.sendCertificate(new Personnummer(personId), utlatandeId, mottagare);

        /* Verify */
        assertEquals("sent", apiResponse.getResultCode());

        ArgumentCaptor<SendCertificateToRecipientType> argument = ArgumentCaptor.forClass(SendCertificateToRecipientType.class);
        verify(sendServiceMock).sendCertificateToRecipient(eq("FKORG"), argument.capture());
        verify(monitoringServiceMock).logCertificateSend(utlatandeId, mottagare);

        SendCertificateToRecipientType actualRequest = argument.getValue();
        assertNotNull(actualRequest.getSkickatTidpunkt());
        assertNotNull(actualRequest.getPatientPersonId().getRoot());
        assertEquals(personId, actualRequest.getPatientPersonId().getExtension());
        assertNotNull(actualRequest.getIntygsId().getRoot());
        assertEquals(utlatandeId, actualRequest.getIntygsId().getExtension());
        assertNotNull(actualRequest.getMottagare().getCodeSystem());
        assertEquals(mottagare, actualRequest.getMottagare().getCode());
        assertNotNull(actualRequest.getSkickatAv().getPersonId().getRoot());
        assertEquals(citizenUsername, actualRequest.getSkickatAv().getPersonId().getExtension());
    }

    @Test
    public void testSendCertificateReturnsValidationError() {
        /* Given */
        SendCertificateToRecipientResponseType response = new SendCertificateToRecipientResponseType();
        response.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.errorResult(
                se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType.VALIDATION_ERROR, "validation error"));

        /* When */
        when(sendServiceMock.sendCertificateToRecipient(any(String.class), any(SendCertificateToRecipientType.class))).thenReturn(response);
        when(citizenService.getCitizen()).thenReturn(mock(Citizen.class));

        /* Then */
        ModuleAPIResponse apiResponse = service.sendCertificate(new Personnummer("19121212-1212"), "1234567890", "FK");

        /* Verify */
        assertEquals("error", apiResponse.getResultCode());
    }

    @Test(expected = SoapFault.class)
    public void testSendCertificateThrowsSoapFault() {

        /* When */
        when(sendServiceMock.sendCertificateToRecipient(any(String.class), any(SendCertificateToRecipientType.class)))
                .thenThrow(new SoapFault("server error", new QName("")));
        when(citizenService.getCitizen()).thenReturn(mock(Citizen.class));

        /* Then */
        service.sendCertificate(new Personnummer("19121212-1212"), "1234567890", "FK");
    }

    private ClinicalProcessCertificateMetaTypeBuilder getClinicalProcessCertificateMetaTypeBuilder(
            UtlatandeStatus unhandled,
            UtlatandeStatus deleted,
            UtlatandeStatus cancelled,
            UtlatandeStatus sent) {
        ClinicalProcessCertificateMetaTypeBuilder builder = new ClinicalProcessCertificateMetaTypeBuilder();
        builder.available(AVAILABLE)
                .certificateId(CERTIFIED_ID)
                .certificateType(TYPE)
                .facilityName(FACILITY_NAME)
                .issuerName(ISSUER_NAME)
                .signDate(signDateTime)
                .validity(validFromDate, validToDate);

        if (unhandled != null) {
            builder.status(unhandled);
        }
        if (deleted != null) {
            builder.status(deleted);
        }
        if (cancelled != null) {
            builder.status(cancelled);
        }
        if (sent != null) {
            builder.status(sent);
        }

        return builder;
    }

}
