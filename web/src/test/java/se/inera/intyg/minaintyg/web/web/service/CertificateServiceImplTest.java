/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

import org.apache.cxf.binding.soap.SoapFault;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.GetRecipientsForCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.GetRecipientsForCertificateResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.GetRecipientsForCertificateType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.RecipientType;
import se.inera.intyg.common.fkparent.support.ResultTypeUtil;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.ModuleAPIResponse;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.exception.ResultTypeErrorException;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.web.util.UtlatandeMetaDataConverter;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v2.ListCertificatesForCitizenResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v2.ListCertificatesForCitizenResponseType;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v2.ListCertificatesForCitizenType;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v2.ListaType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.SendCertificateToRecipientResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.SendCertificateToRecipientResponseType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.SendCertificateToRecipientType;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v1.SetCertificateStatusResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v1.SetCertificateStatusResponseType;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v1.SetCertificateStatusType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v2.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

import javax.xml.namespace.QName;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private GetRecipientsForCertificateResponderInterface getRecipientsService;

    @Mock
    private IntygTextsService intygTextsService;

    @Mock
    private MonitoringLogService monitoringServiceMock;

    @Mock
    private IntygModuleRegistry moduleRegistry;

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

        Utlatande utl = buildUtlatande(pnr);
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.SENT, part, LocalDateTime.now())));
        CertificateResponse cert = new CertificateResponse(document, utl, meta, false);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);

        assertTrue(res.isPresent());

        assertEquals(1, res.get().getMetaData().getStatus().size());
        assertEquals(CertificateState.SENT, res.get().getMetaData().getStatus().get(0).getType());
        assertEquals(part, res.get().getMetaData().getStatus().get(0).getTarget());
        assertEquals(document, res.get().getInternalModel());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA));
    }

    @Test
    public void testGetUtlatandeCivicRegistrationNumberWithoutDash() throws Exception {
        final String document = "document";
        final String part = "part";
        final String pnr = "19121212-1212";
        final String pnrWithoutDash = "191212121212";

        Utlatande utl = buildUtlatande(pnrWithoutDash);
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.SENT, part, LocalDateTime.now())));
        CertificateResponse cert = new CertificateResponse(document, utl, meta, false);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);

        assertTrue(res.isPresent());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA));
    }

    @Test
    public void testGetUtlatandeWrongCivicRegistrationNumber() throws Exception {
        final String document = "document";
        final String part = "part";
        final String pnr = "19121212-1212";
        final String anotherPnr = "19101010-1010";

        Utlatande utl = buildUtlatande(anotherPnr);
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.SENT, part, LocalDateTime.now())));
        CertificateResponse cert = new CertificateResponse(document, utl, meta, false);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);

        assertFalse(res.isPresent());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA));
    }

    @Test
    public void testGetUtlatandeRevoked() throws Exception {
        final String document = "document";
        final String part = "part";
        final String pnr = "19121212-1212";

        Utlatande utl = buildUtlatande(pnr);
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.CANCELLED, part, LocalDateTime.now())));
        CertificateResponse cert = new CertificateResponse(document, utl, meta, false);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);

        assertFalse(res.isPresent()); // don't return revoked certificate

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA));
    }

    @Test(expected = ExternalWebServiceCallFailedException.class)
    public void testGetUtlatandeModuleException() throws Exception {
        final String part = "part";
        final String pnr = "19121212-1212";

        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.CANCELLED, part, LocalDateTime.now())));
        ModuleApi api = mock(ModuleApi.class);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(api);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(PartKod.INVANA))).thenThrow(new ModuleException("error"));

        service.getUtlatande(CERTIFICATE_TYPE, new Personnummer(pnr), CERTIFICATE_ID);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetCertificates() {
        final String pnr = "19121212-1212";
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setIntygsLista(new ListaType());
        response.getIntygsLista().getIntyg().add(buildIntyg());
        response.setResult(ResultTypeUtil.okResult());

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
        response.setResult(ResultTypeUtil.okResult());

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
        response.setResult(ResultTypeUtil
                .errorResult(se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType.APPLICATION_ERROR, "an error"));

        when(listServiceMock.listCertificatesForCitizen(any(String.class), any(ListCertificatesForCitizenType.class))).thenReturn(response);

        service.getCertificates(new Personnummer("19121212-1212"), false);
    }

    @Test
    public void testGetRecipientsForCertificate() {
        final String type = "fk7263";
        final String recipientId = "recipient-id";
        final String recipientName = "recipient-name";
        GetRecipientsForCertificateResponseType responseType = new GetRecipientsForCertificateResponseType();
        responseType.setResult(se.inera.intyg.common.fk7263.schemas.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil.okResult());
        RecipientType rt = new RecipientType();
        rt.setId(recipientId);
        rt.setName(recipientName);
        responseType.getRecipient().add(rt);
        when(getRecipientsService.getRecipientsForCertificate(anyString(), any(GetRecipientsForCertificateType.class))).thenReturn(responseType );
        List<UtlatandeRecipient> recipientList = service.getRecipientsForCertificate(type);
        assertNotNull(recipientList);
        assertEquals(1, recipientList.size());
        assertEquals(recipientId, recipientList.get(0).getId());
        assertEquals(recipientName, recipientList.get(0).getName());

        ArgumentCaptor<GetRecipientsForCertificateType> requestCaptor = ArgumentCaptor.forClass(GetRecipientsForCertificateType.class);
        verify(getRecipientsService).getRecipientsForCertificate(anyString(), requestCaptor.capture());
        assertEquals(type, requestCaptor.getValue().getCertificateType());
    }

    @Test(expected = ResultTypeErrorException.class)
    public void testGetRecipientsForCertificateErrorResponse() {
        final String type = "fk7263";
        GetRecipientsForCertificateResponseType responseType = new GetRecipientsForCertificateResponseType();
        responseType.setResult(se.inera.intyg.common.fk7263.schemas.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil.errorResult(se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType.APPLICATION_ERROR, "error"));
        when(getRecipientsService.getRecipientsForCertificate(anyString(), any(GetRecipientsForCertificateType.class))).thenReturn(responseType );
        service.getRecipientsForCertificate(type);
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
        response2.setResult(ResultTypeUtil.okResult());
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

    @SuppressWarnings("unchecked")
    @Test(expected = ExternalWebServiceCallFailedException.class)
    public void testArchiveCertificateErrorResponse() throws Exception {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(setCertificateStatusService.setCertificateStatus(anyString(), any(SetCertificateStatusType.class)))
                .thenReturn(response);

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType());
        response2.getIntygsLista().getIntyg().add(buildIntyg());
        response2.setResult(ResultTypeUtil.okResult());
        when(listServiceMock.listCertificatesForCitizen(anyString(), any(ListCertificatesForCitizenType.class))).thenReturn(response2);
        UtlatandeMetaData umd = new UtlatandeMetaData(CERTIFICATE_ID, CERTIFICATE_TYPE, ISSUER_NAME, FACILITY_NAME, LocalDateTime.now(), "true", "", null);
        when(utlatandeMetaDataConverter.convert(any(List.class), eq(false))).thenReturn(Arrays.asList(umd));

        service.archiveCertificate(CERTIFICATE_ID, new Personnummer(pnr));
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
        response2.setResult(ResultTypeUtil.okResult());
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
        response2.setResult(ResultTypeUtil.okResult());
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
        response2.setResult(ResultTypeUtil.okResult());
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
        response.setResult(ResultTypeUtil.okResult());
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
        response.setResult(ResultTypeUtil.errorResult(
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

    @Test
    public void testGetQuestions() {
        String questionString = "{questions}";
        final String type = "luse";
        final String version = "1.0";
        when(intygTextsService.getIntygTexts(type, version)).thenReturn(questionString);

        String res = service.getQuestions(type, version);
        assertEquals(questionString, res);
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

    private Utlatande buildUtlatande(String pnr) {
        Utlatande utlatande = mock(Utlatande.class);
        GrundData grunddata = new GrundData();
        grunddata.setPatient(new se.inera.intyg.common.support.model.common.internal.Patient());
        grunddata.getPatient().setPersonId(new Personnummer(pnr));
        when(utlatande.getTyp()).thenReturn(CERTIFICATE_TYPE);
        when(utlatande.getId()).thenReturn(CERTIFICATE_ID);
        when(utlatande.getGrundData()).thenReturn(grunddata);
        return utlatande;
    }

}
