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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapFault;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.ModuleAPIResponse;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeWithMeta;
import se.inera.intyg.minaintyg.web.web.util.UtlatandeMetaDataConverter;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    private static final String ISSUER_NAME = "issuerName";
    private static final String FACILITY_NAME = "facilityName";
    private static final String CERTIFICATE_ID = "certificateId";
    private static final String CERTIFICATE_TYPE = "certificateType";

    @Mock
    private ListCertificatesForCitizenResponderInterface listServiceMock;

    @Mock
    private SendCertificateToRecipientResponderInterface sendServiceMock;

    @Mock
    private MonitoringLogService monitoringServiceMock;

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @Mock
    private GetCertificateResponderInterface getCertificateService;

    @Mock
    private CitizenService citizenService;

    @Mock
    private SetCertificateStatusResponderInterface setCertificateStatusService;

    @Mock
    private UtlatandeMetaDataConverter utlatandeMetaDataConverter;

    @InjectMocks
    private CertificateServiceImpl service;

    @Before
    public void inject_fields() {
        service.setLogicalAddress("FKORG");
    }

    @Before
    public void mockObjectMapper() {
        CertificateServiceImpl.objectMapper = mock(CustomObjectMapper.class);
    }

    @Test
    public void testGetUtlatande() throws Exception {
        final String document = "document";
        final String part = "part";
        final String pnr = "19121212-1212";

        when(moduleRegistry.moduleExists(CERTIFICATE_TYPE)).thenReturn(true);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getUtlatandeFromIntyg(any())).thenReturn(mock(Utlatande.class));
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE.toLowerCase())).thenReturn(api);
        when(getCertificateService.getCertificate(anyString(), any())).thenReturn(createGetCertificateResponseType(part, pnr));
        when(CertificateServiceImpl.objectMapper.writeValueAsString(any())).thenReturn(document);

        Optional<UtlatandeWithMeta> res = service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);

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

    @Test
    public void testGetUtlatandeCivicRegistrationNumberWithoutDash() throws Exception {
        final String document = "document";
        final String part = "part";
        final String pnr = "19121212-1212";
        final String pnrWithoutDash = "191212121212";

        when(moduleRegistry.moduleExists(CERTIFICATE_TYPE)).thenReturn(true);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getUtlatandeFromIntyg(any())).thenReturn(mock(Utlatande.class));
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE.toLowerCase())).thenReturn(api);
        when(getCertificateService.getCertificate(anyString(), any())).thenReturn(createGetCertificateResponseType(part, pnrWithoutDash));
        when(CertificateServiceImpl.objectMapper.writeValueAsString(any())).thenReturn(document);

        Optional<UtlatandeWithMeta> res = service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);

        assertNotNull(res);
        assertTrue(res.isPresent());

        verify(getCertificateService, times(1)).getCertificate(anyString(), any());
    }

    @Test
    public void testGetUtlatandeWrongCivicRegistrationNumber() throws Exception {
        final String document = "document";
        final String id = "lisu";
        final String part = "part";
        final String pnr = "19121212-1212";
        final String anotherPnr = "19101010-1010";

        when(moduleRegistry.moduleExists(id)).thenReturn(true);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getUtlatandeFromIntyg(any())).thenReturn(mock(Utlatande.class));
        when(moduleRegistry.getModuleApi(id)).thenReturn(api);
        when(getCertificateService.getCertificate(anyString(), any())).thenReturn(createGetCertificateResponseType(part, anotherPnr));
        when(CertificateServiceImpl.objectMapper.writeValueAsString(any())).thenReturn(document);

        Optional<UtlatandeWithMeta> res = service.getUtlatande(id, new Personnummer(pnr), CERTIFICATE_ID);

        assertNotNull(res);
        assertFalse(res.isPresent());

        verify(getCertificateService, times(1)).getCertificate(anyString(), any());
    }

    private GetCertificateResponseType createGetCertificateResponseType(final String part, final String pnr) {
        GetCertificateResponseType response = new GetCertificateResponseType();
        Intyg intyg = buildIntyg();

        intyg.getStatus().add(createStatus(StatusKod.SENTTO.name(), part));
        intyg.getStatus().add(createStatus(StatusKod.RECEIV.name(), part));

        intyg.getTyp().setCode(CERTIFICATE_TYPE);

        intyg.setPatient(new Patient());
        intyg.getPatient().setPersonId(new PersonId());
        intyg.getPatient().getPersonId().setExtension(pnr);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetCertificates() {
        final String pnr = "19121212-1212";
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setIntygsLista(new ListaType());
        response.getIntygsLista().getIntyg().add(buildIntyg());
        response.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates(new Personnummer(pnr), false);

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(utlatandeMetaDataConverter).convert(listCaptor.capture(), eq(false));

        assertEquals(1, listCaptor.getValue().size());
        assertEquals(CERTIFICATE_ID, ((Intyg) listCaptor.getValue().get(0)).getIntygsId().getExtension());

        ArgumentCaptor<ListCertificatesForCitizenType> paramCaptor = ArgumentCaptor.forClass(ListCertificatesForCitizenType.class);
        verify(listServiceMock).listCertificatesForCitizen(anyString(), paramCaptor.capture());
        assertFalse(paramCaptor.getValue().isArkiverade());
        assertNotNull(paramCaptor.getValue().getPersonId().getRoot());
        assertEquals(pnr.replace("-", ""), paramCaptor.getValue().getPersonId().getExtension());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetCertificatesArkiveradTrue() {
        final String pnr = "19121212-1212";
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setIntygsLista(new ListaType());
        response.getIntygsLista().getIntyg().add(buildIntyg());
        response.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates(new Personnummer(pnr), true);

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(utlatandeMetaDataConverter).convert(listCaptor.capture(), eq(true));

        assertEquals(1, listCaptor.getValue().size());
        assertEquals(CERTIFICATE_ID, ((Intyg) listCaptor.getValue().get(0)).getIntygsId().getExtension());

        ArgumentCaptor<ListCertificatesForCitizenType> paramCaptor = ArgumentCaptor.forClass(ListCertificatesForCitizenType.class);
        verify(listServiceMock).listCertificatesForCitizen(anyString(), paramCaptor.capture());
        assertTrue(paramCaptor.getValue().isArkiverade());
        assertNotNull(paramCaptor.getValue().getPersonId().getRoot());
        assertEquals(pnr.replace("-", ""), paramCaptor.getValue().getPersonId().getExtension());
    }

    @Test(expected = ExternalWebServiceCallFailedException.class)
    public void testGetCertificatesFailureHandling() {
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil
                .errorResult(se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType.APPLICATION_ERROR, "an error"));

        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates(new Personnummer("19121212-1212"), false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testArchiveCertificate() throws Exception {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(setCertificateStatusService.setCertificateStatus(anyString(), any(SetCertificateStatusType.class)))
                .thenReturn(response);

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType());
        response2.getIntygsLista().getIntyg().add(buildIntyg());
        response2.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());
        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response2);
        UtlatandeMetaData umd = new UtlatandeMetaData(CERTIFICATE_ID, CERTIFICATE_TYPE, ISSUER_NAME, FACILITY_NAME, LocalDateTime.now(), "true", "", null);
        when(utlatandeMetaDataConverter.convert(any(List.class), eq(false))).thenReturn(Arrays.asList(umd));

        UtlatandeMetaData result = service.archiveCertificate(CERTIFICATE_ID, new Personnummer(pnr));
        assertEquals(CERTIFICATE_ID, result.getId());
        assertEquals(String.valueOf(false), result.getAvailable());

        ArgumentCaptor<SetCertificateStatusType> paramCaptor = ArgumentCaptor.forClass(SetCertificateStatusType.class);
        verify(setCertificateStatusService).setCertificateStatus(anyString(), paramCaptor.capture());
        assertNotNull(paramCaptor.getValue().getIntygsId().getRoot());
        assertEquals(CERTIFICATE_ID, paramCaptor.getValue().getIntygsId().getExtension());
        assertNotNull(paramCaptor.getValue().getPart().getCodeSystem());
        assertNotNull(paramCaptor.getValue().getPart().getDisplayName());
        assertEquals(PartKod.INVANA.name(), paramCaptor.getValue().getPart().getCode());
        assertNotNull(paramCaptor.getValue().getStatus().getCodeSystem());
        assertNotNull(paramCaptor.getValue().getStatus().getDisplayName());
        assertEquals(StatusKod.DELETE.name(), paramCaptor.getValue().getStatus().getCode());
        assertNotNull(paramCaptor.getValue().getTidpunkt());
    }

    @Test
    public void testArchiveCertificateWrongPnr() {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(setCertificateStatusService.setCertificateStatus(anyString(), any(SetCertificateStatusType.class)))
        .thenReturn(response);
        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType()); // no certificates
        response2.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());
        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response2);

        try {
            service.archiveCertificate(CERTIFICATE_ID, new Personnummer(pnr));
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid certificate"));
        }

        verify(setCertificateStatusService, never()).setCertificateStatus(anyString(), any(SetCertificateStatusType.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRestoreCertificate() throws Exception {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(setCertificateStatusService.setCertificateStatus(anyString(), any(SetCertificateStatusType.class)))
                .thenReturn(response);
        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType());
        response2.getIntygsLista().getIntyg().add(buildIntyg());
        response2.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());
        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response2);
        UtlatandeMetaData umd = new UtlatandeMetaData(CERTIFICATE_ID, CERTIFICATE_TYPE, ISSUER_NAME, FACILITY_NAME, LocalDateTime.now(), "true", "", null);
        when(utlatandeMetaDataConverter.convert(any(List.class), eq(true))).thenReturn(Arrays.asList(umd));

        UtlatandeMetaData result = service.restoreCertificate(CERTIFICATE_ID, new Personnummer(pnr));
        assertEquals(CERTIFICATE_ID, result.getId());
        assertEquals(String.valueOf(true), result.getAvailable());

        ArgumentCaptor<SetCertificateStatusType> paramCaptor = ArgumentCaptor.forClass(SetCertificateStatusType.class);
        verify(setCertificateStatusService).setCertificateStatus(anyString(), paramCaptor.capture());
        assertNotNull(paramCaptor.getValue().getIntygsId().getRoot());
        assertEquals(CERTIFICATE_ID, paramCaptor.getValue().getIntygsId().getExtension());
        assertNotNull(paramCaptor.getValue().getPart().getCodeSystem());
        assertNotNull(paramCaptor.getValue().getPart().getDisplayName());
        assertEquals(PartKod.INVANA.name(), paramCaptor.getValue().getPart().getCode());
        assertNotNull(paramCaptor.getValue().getStatus().getCodeSystem());
        assertNotNull(paramCaptor.getValue().getStatus().getDisplayName());
        assertEquals(StatusKod.RESTOR.name(), paramCaptor.getValue().getStatus().getCode());
        assertNotNull(paramCaptor.getValue().getTidpunkt());
    }

    @Test
    public void testRestoreCertificateWrongPnr() {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(setCertificateStatusService.setCertificateStatus(anyString(), any(SetCertificateStatusType.class)))
        .thenReturn(response);
        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType()); // no certificates
        response2.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil.okResult());
        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response2);

        try {
            service.restoreCertificate(CERTIFICATE_ID, new Personnummer(pnr));
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid certificate"));
        }

        verify(setCertificateStatusService, never()).setCertificateStatus(anyString(), any(SetCertificateStatusType.class));
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
        assertEquals(personId.replace("-", ""), actualRequest.getPatientPersonId().getExtension());
        assertNotNull(actualRequest.getIntygsId().getRoot());
        assertEquals(utlatandeId, actualRequest.getIntygsId().getExtension());
        assertNotNull(actualRequest.getMottagare().getCodeSystem());
        assertEquals("FKASSA", actualRequest.getMottagare().getCode());
        assertNotNull(actualRequest.getSkickatAv().getPersonId().getRoot());
        assertEquals(citizenUsername.replace("-", ""), actualRequest.getSkickatAv().getPersonId().getExtension());
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

    private Intyg buildIntyg() {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension(CERTIFICATE_ID);
        intyg.setTyp(new TypAvIntyg());
        intyg.getTyp().setCode(CERTIFICATE_TYPE);
        intyg.setSkapadAv(new HosPersonal());
        intyg.getSkapadAv().setFullstandigtNamn(ISSUER_NAME);
        intyg.getSkapadAv().setEnhet(new Enhet());
        intyg.getSkapadAv().getEnhet().setEnhetsnamn(FACILITY_NAME);
        intyg.setSigneringstidpunkt(LocalDateTime.now());
        return intyg;
    }

}
