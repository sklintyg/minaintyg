/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.service;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.GetRecipientsForCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.GetRecipientsForCertificateResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.GetRecipientsForCertificateType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.RecipientType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.IntygRelations;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.ListRelationsForCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.ListRelationsForCertificateResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.ListRelationsForCertificateType;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
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
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.exception.ResultTypeErrorException;
import se.inera.intyg.minaintyg.web.security.Citizen;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.util.UtlatandeMetaDataConverter;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListCertificatesForCitizenResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListCertificatesForCitizenResponseType;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListCertificatesForCitizenType;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListaType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientResponseType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientType;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v2.SetCertificateStatusResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v2.SetCertificateStatusResponseType;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v2.SetCertificateStatusType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
    private static final String CERTIFICATE_TYPE_VERSION = "1.0";

    private static final String INTYGSTJANST_LOGICAL_ADDRESS = "INTYGSTJANST_LOGICAL_ADDRESS";
    private static final String MINAINTYG_RECIPIENT_ID = "INVANA";
    private static final String FK_RECIPIENT_ID = "FKASSA";
    private static final String TS_RECIPIENT_ID = "TRANSP";

    @Mock
    private ListCertificatesForCitizenResponderInterface listServiceMock;

    @Mock
    private SendCertificateToRecipientResponderInterface sendServiceMock;

    @Mock
    private GetRecipientsForCertificateResponderInterface getRecipientsService;

    @Mock
    private ListRelationsForCertificateResponderInterface listRelationsService;

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
        service.setLogicalAddress(INTYGSTJANST_LOGICAL_ADDRESS);
    }

    @Before
    public void mockObjectMapper() {
        CertificateServiceImpl.objectMapper = mock(CustomObjectMapper.class);
    }

    @Before
    public void mockListRelationsService() {
        ListRelationsForCertificateResponseType resp = new ListRelationsForCertificateResponseType();
        when(listRelationsService.listRelationsForCertificate(anyString(), any(ListRelationsForCertificateType.class)))
                .thenReturn(resp);
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
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, createPnr(pnr), CERTIFICATE_ID);

        assertTrue(res.isPresent());

        assertEquals(1, res.get().getMetaData().getStatus().size());
        assertEquals(CertificateState.SENT, res.get().getMetaData().getStatus().get(0).getType());
        assertEquals(part, res.get().getMetaData().getStatus().get(0).getTarget());
        assertEquals(document, res.get().getInternalModel());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID));
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
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, createPnr(pnr), CERTIFICATE_ID);

        assertTrue(res.isPresent());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID));
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
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, createPnr(pnr), CERTIFICATE_ID);

        assertFalse(res.isPresent());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID));
    }

    @Test
    public void testGetUtlatandeRevoked() throws Exception {
        final String document = "document";
        final String part = "part";
        final String pnr = "19121212-1212";

        Utlatande utl = buildUtlatande(pnr);
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.CANCELLED, part, LocalDateTime.now())));
        CertificateResponse cert = new CertificateResponse(document, utl, meta, true);
        ModuleApi api = mock(ModuleApi.class);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID))).thenReturn(cert);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION)).thenReturn(api);

        Optional<CertificateResponse> res = service.getUtlatande(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, createPnr(pnr), CERTIFICATE_ID);

        assertTrue(res.isPresent());
        assertTrue(res.get().isRevoked());

        verify(api, times(1)).getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID));
    }

    @Test(expected = ExternalWebServiceCallFailedException.class)
    public void testGetUtlatandeModuleException() throws Exception {
        final String part = "part";
        final String pnr = "19121212-1212";

        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(Arrays.asList(new Status(CertificateState.CANCELLED, part, LocalDateTime.now())));
        ModuleApi api = mock(ModuleApi.class);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION)).thenReturn(api);
        when(api.getCertificate(eq(CERTIFICATE_ID), anyString(), eq(MINAINTYG_RECIPIENT_ID))).thenThrow(new ModuleException("error"));

        service.getUtlatande(CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, createPnr(pnr), CERTIFICATE_ID);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetCertificates() {
        final String pnr = "19121212-1212";
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setIntygsLista(new ListaType());
        response.getIntygsLista().getIntyg().add(buildIntyg());
        response.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response);

        service.getCertificates(createPnr(pnr), false);

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(utlatandeMetaDataConverter).convert(listCaptor.capture(), anyList(), eq(false));

        assertEquals(1, listCaptor.getValue().size());
        assertEquals(CERTIFICATE_ID, ((Intyg) listCaptor.getValue().get(0)).getIntygsId().getExtension());

        ArgumentCaptor<ListCertificatesForCitizenType> paramCaptor = ArgumentCaptor.forClass(ListCertificatesForCitizenType.class);
        verify(listServiceMock).listCertificatesForCitizen(isNull(), paramCaptor.capture());
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

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response);

        service.getCertificates(createPnr(pnr), true);

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(utlatandeMetaDataConverter).convert(listCaptor.capture(), anyList(), eq(true));

        assertEquals(1, listCaptor.getValue().size());
        assertEquals(CERTIFICATE_ID, ((Intyg) listCaptor.getValue().get(0)).getIntygsId().getExtension());

        ArgumentCaptor<ListCertificatesForCitizenType> paramCaptor = ArgumentCaptor.forClass(ListCertificatesForCitizenType.class);
        verify(listServiceMock).listCertificatesForCitizen(or(isNull(), anyString()), paramCaptor.capture());
        assertTrue(paramCaptor.getValue().isArkiverade());
        assertNotNull(paramCaptor.getValue().getPersonId().getRoot());
        assertEquals(pnr.replace("-", ""), paramCaptor.getValue().getPersonId().getExtension());
    }

    @Test(expected = ExternalWebServiceCallFailedException.class)
    public void testGetCertificatesFailureHandling() {
        ListCertificatesForCitizenResponseType response = new ListCertificatesForCitizenResponseType();
        response.setResult(ResultTypeUtil
                .errorResult(se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType.APPLICATION_ERROR, "an error"));

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response);

        service.getCertificates(createPnr("19121212-1212"), false);
    }

    @Test
    public void testGetRecipientsForCertificate() {
        final String intygsId = "intygs-id";
        final String recipientId = "recipient-id";
        final String recipientName = "recipient-name";

        GetRecipientsForCertificateResponseType responseType = new GetRecipientsForCertificateResponseType();
        responseType.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.v1.utils.ResultTypeUtil.okResult());

        RecipientType rt = new RecipientType();
        rt.setId(recipientId);
        rt.setName(recipientName);
        responseType.getRecipient().add(rt);

        when(getRecipientsService.getRecipientsForCertificate(anyString(), any(GetRecipientsForCertificateType.class))).thenReturn(responseType );
        List<UtlatandeRecipient> recipientList = service.getRecipientsForCertificate(intygsId);

        assertNotNull(recipientList);
        assertEquals(1, recipientList.size());
        assertEquals(recipientId, recipientList.get(0).getId());
        assertEquals(recipientName, recipientList.get(0).getName());

        ArgumentCaptor<GetRecipientsForCertificateType> requestCaptor = ArgumentCaptor.forClass(GetRecipientsForCertificateType.class);
        verify(getRecipientsService).getRecipientsForCertificate(anyString(), requestCaptor.capture());
        assertEquals(intygsId, requestCaptor.getValue().getCertificateId());
    }

    @Test(expected = ResultTypeErrorException.class)
    public void testGetRecipientsForCertificateErrorResponse() {
        final String type = "fk7263";
        GetRecipientsForCertificateResponseType responseType = new GetRecipientsForCertificateResponseType();
        responseType.setResult(se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.v1.utils.ResultTypeUtil.errorResult(se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType.APPLICATION_ERROR, "error"));
        when(getRecipientsService.getRecipientsForCertificate(anyString(), any(GetRecipientsForCertificateType.class))).thenReturn(responseType );
        service.getRecipientsForCertificate(type);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testArchiveCertificate() throws Exception {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(setCertificateStatusService.setCertificateStatus(
                or(isNull(), anyString()),
                any(SetCertificateStatusType.class))
        ).thenReturn(response);

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType());
        response2.getIntygsLista().getIntyg().add(buildIntyg());
        response2.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response2);

        UtlatandeMetaData umd = new UtlatandeMetaData(CERTIFICATE_ID, CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, ISSUER_NAME, FACILITY_NAME, LocalDateTime.now(), "true", "", null, new ArrayList<>());
        when(utlatandeMetaDataConverter.convert(any(List.class), anyList(), eq(false))).thenReturn(ImmutableList.of(umd));

        UtlatandeMetaData result = service.archiveCertificate(CERTIFICATE_ID, createPnr(pnr));
        assertEquals(CERTIFICATE_ID, result.getId());
        assertEquals(String.valueOf(false), result.getAvailable());

        ArgumentCaptor<SetCertificateStatusType> paramCaptor = ArgumentCaptor.forClass(SetCertificateStatusType.class);
        verify(setCertificateStatusService).setCertificateStatus(anyString(), paramCaptor.capture());
        assertNotNull(paramCaptor.getValue().getIntygsId().getRoot());
        assertEquals(CERTIFICATE_ID, paramCaptor.getValue().getIntygsId().getExtension());
        assertNotNull(paramCaptor.getValue().getPart().getCodeSystem());
        assertEquals(MINAINTYG_RECIPIENT_ID, paramCaptor.getValue().getPart().getCode());
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

        when(setCertificateStatusService.setCertificateStatus(
                or(isNull(), anyString()),
                any(SetCertificateStatusType.class))
        ).thenReturn(response);

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType());
        response2.getIntygsLista().getIntyg().add(buildIntyg());
        response2.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response2);

        UtlatandeMetaData umd = new UtlatandeMetaData(CERTIFICATE_ID, CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, ISSUER_NAME,
                FACILITY_NAME, LocalDateTime.now(), "true", "", null, null);
        when(utlatandeMetaDataConverter.convert(any(List.class), any(List.class), eq(false))).thenReturn(ImmutableList.of(umd));

        service.archiveCertificate(CERTIFICATE_ID, createPnr(pnr));
    }

    @Test
    public void testArchiveCertificateWrongPnr() {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType()); // no certificates
        response2.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response2);

        try {
            service.archiveCertificate(CERTIFICATE_ID, createPnr(pnr));
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid certificate"));
        }

        verify(setCertificateStatusService, never()).setCertificateStatus(isNull(), any(SetCertificateStatusType.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRestoreCertificate() throws Exception {
        final String pnr = "19121212-1212";

        SetCertificateStatusResponseType response = new SetCertificateStatusResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(setCertificateStatusService.setCertificateStatus(
                or(isNull(), anyString()),
                any(SetCertificateStatusType.class))
        ).thenReturn(response);

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType());
        response2.getIntygsLista().getIntyg().add(buildIntyg());
        response2.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response2);

        UtlatandeMetaData umd = new UtlatandeMetaData(CERTIFICATE_ID, CERTIFICATE_TYPE, CERTIFICATE_TYPE_VERSION, ISSUER_NAME,
                FACILITY_NAME, LocalDateTime.now(), "true", "", null, null);
        when(utlatandeMetaDataConverter.convert(any(List.class), anyList(), eq(true))).thenReturn(Arrays.asList(umd));

        UtlatandeMetaData result = service.restoreCertificate(CERTIFICATE_ID, createPnr(pnr));
        assertEquals(CERTIFICATE_ID, result.getId());
        assertEquals(String.valueOf(true), result.getAvailable());

        ArgumentCaptor<SetCertificateStatusType> paramCaptor = ArgumentCaptor.forClass(SetCertificateStatusType.class);
        verify(setCertificateStatusService).setCertificateStatus(anyString(), paramCaptor.capture());
        assertNotNull(paramCaptor.getValue().getIntygsId().getRoot());
        assertEquals(CERTIFICATE_ID, paramCaptor.getValue().getIntygsId().getExtension());
        assertNotNull(paramCaptor.getValue().getPart().getCodeSystem());

        assertEquals(MINAINTYG_RECIPIENT_ID, paramCaptor.getValue().getPart().getCode());
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

        ListCertificatesForCitizenResponseType response2 = new ListCertificatesForCitizenResponseType();
        response2.setIntygsLista(new ListaType()); // no certificates
        response2.setResult(ResultTypeUtil.okResult());

        when(listServiceMock.listCertificatesForCitizen(
                or(isNull(), anyString()),
                any(ListCertificatesForCitizenType.class))
        ).thenReturn(response2);

        try {
            service.restoreCertificate(CERTIFICATE_ID, createPnr(pnr));
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid certificate"));
        }

        verify(setCertificateStatusService, never()).setCertificateStatus(anyString(), any(SetCertificateStatusType.class));
    }

    /**
     * Send to multiple recipients, where one should fail and one succeed
     */
    @Test
    public void testSendCertificateMultiple() {

        String personId = "19121212-1212";
        String citizenUsername = "19121212-1212";
        String utlatandeId = "1234567890";
        List<String> recipients = Arrays.asList(FK_RECIPIENT_ID, TS_RECIPIENT_ID);

        SendCertificateToRecipientResponseType successResponse = new SendCertificateToRecipientResponseType();
        successResponse.setResult(ResultTypeUtil.okResult());

        SendCertificateToRecipientResponseType errorResponse = new SendCertificateToRecipientResponseType();
        errorResponse.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "Fel blev det"));

        Citizen citizen = mock(Citizen.class);

        /* When */
        // NOTE: This mocking assumes that they are processed in the order that they are enumerated in the recipients
        // list argument
        when(sendServiceMock.sendCertificateToRecipient(any(String.class), any(SendCertificateToRecipientType.class)))
                .thenReturn(successResponse)
                .thenReturn(errorResponse);

        when(citizen.getUsername()).thenReturn(citizenUsername);
        when(citizenService.getCitizen()).thenReturn(citizen);

        /* Then */
        final List<SendToRecipientResult> apiResponse = service.sendCertificate(createPnr(personId), utlatandeId, recipients);

        /* Verify */
        assertEquals(2, apiResponse.size());
        SendToRecipientResult fkResult = apiResponse.stream().filter(sr -> sr.getRecipientId().equals(FK_RECIPIENT_ID))
                .findFirst().get();
        assertTrue(fkResult.isSent());
        assertNotNull(fkResult.getTimestamp());

        SendToRecipientResult tsResult = apiResponse.stream().filter(sr -> sr.getRecipientId().equals(TS_RECIPIENT_ID))
                .findFirst().get();
        assertFalse(tsResult.isSent());
        assertNull(tsResult.getTimestamp());

        ArgumentCaptor<SendCertificateToRecipientType> arguments = ArgumentCaptor.forClass(SendCertificateToRecipientType.class);

        // NOTE: This mocking assumes that they are processed in the order that they are enumerated in the recipients
        // list
        verify(sendServiceMock, times(2)).sendCertificateToRecipient(eq(INTYGSTJANST_LOGICAL_ADDRESS), arguments.capture());
        verify(monitoringServiceMock).logCertificateSend(utlatandeId, FK_RECIPIENT_ID);

        List<SendCertificateToRecipientType> capturedArguments = arguments.getAllValues();
        SendCertificateToRecipientType actualRequestFK = capturedArguments.get(0);
        assertNotNull(actualRequestFK.getSkickatTidpunkt());
        assertNotNull(actualRequestFK.getPatientPersonId().getRoot());
        assertEquals(personId.replace("-", ""), actualRequestFK.getPatientPersonId().getExtension());
        assertNotNull(actualRequestFK.getIntygsId().getRoot());
        assertEquals(utlatandeId, actualRequestFK.getIntygsId().getExtension());
        assertNotNull(actualRequestFK.getMottagare().getCodeSystem());
        assertEquals(FK_RECIPIENT_ID, actualRequestFK.getMottagare().getCode());
        assertNotNull(actualRequestFK.getSkickatAv().getPersonId().getRoot());
        assertEquals(citizenUsername.replace("-", ""), actualRequestFK.getSkickatAv().getPersonId().getExtension());

        SendCertificateToRecipientType actualRequestTS = capturedArguments.get(1);
        assertNotNull(actualRequestTS.getSkickatTidpunkt());
        assertNotNull(actualRequestTS.getPatientPersonId().getRoot());
        assertEquals(personId.replace("-", ""), actualRequestTS.getPatientPersonId().getExtension());
        assertNotNull(actualRequestTS.getIntygsId().getRoot());
        assertEquals(utlatandeId, actualRequestTS.getIntygsId().getExtension());
        assertNotNull(actualRequestTS.getMottagare().getCodeSystem());
        assertEquals(TS_RECIPIENT_ID, actualRequestTS.getMottagare().getCode());
        assertNotNull(actualRequestTS.getSkickatAv().getPersonId().getRoot());
        assertEquals(citizenUsername.replace("-", ""), actualRequestTS.getSkickatAv().getPersonId().getExtension());
    }

    @Test
    public void testSendCertificateHandlesValidationError() {
        SendCertificateToRecipientResponseType response = new SendCertificateToRecipientResponseType();
        response.setResult(ResultTypeUtil.errorResult(
                se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType.VALIDATION_ERROR, "validation error"));

        when(citizenService.getCitizen()).thenReturn(mock(Citizen.class));

        final List<SendToRecipientResult> apiResponse = service.sendCertificate(createPnr("19121212-1212"), "1234567890", Arrays.asList("FKASSA"));

        SendToRecipientResult fkResult = apiResponse.stream().filter(sr -> sr.getRecipientId().equals(FK_RECIPIENT_ID))
                .findFirst().get();
        assertFalse(fkResult.isSent());
    }

    @Test
    public void testSendCertificateHandlesSoapFault() {

        when(citizenService.getCitizen()).thenReturn(mock(Citizen.class));
        final List<SendToRecipientResult> apiResponse = service.sendCertificate(createPnr("19121212-1212"), "1234567890", Arrays.asList("FKASSA"));
        SendToRecipientResult fkResult = apiResponse.stream().filter(sr -> sr.getRecipientId().equals(FK_RECIPIENT_ID))
                .findFirst().get();
        assertFalse(fkResult.isSent());
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

    @Test
    public void testGetRelationsForCertificate() {
        ListRelationsForCertificateResponseType resp = new ListRelationsForCertificateResponseType();
        IntygRelations ir = new IntygRelations();

        resp.getIntygRelation().add(ir);
        when(listRelationsService.listRelationsForCertificate(anyString(), any(ListRelationsForCertificateType.class)))
                .thenReturn(resp);

        List<IntygRelations> relationsForCertificates = service.getRelationsForCertificates(Arrays.asList("intyg-123"));
        assertEquals(1, relationsForCertificates.size());
    }

    @Test
    public void testGetRelationsForCertificateNoIds() {
        List<IntygRelations> relationsForCertificates = service.getRelationsForCertificates(Collections.emptyList());
        assertEquals(0, relationsForCertificates.size());
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
        grunddata.getPatient().setPersonId(createPnr(pnr));
        when(utlatande.getTyp()).thenReturn(CERTIFICATE_TYPE);
        when(utlatande.getId()).thenReturn(CERTIFICATE_ID);
        when(utlatande.getGrundData()).thenReturn(grunddata);
        return utlatande;
    }

    private Personnummer createPnr(String pnr) {
        return Personnummer.createPersonnummer(pnr)
                .orElseThrow(() -> new IllegalArgumentException("Could not parse passed personnummer: " + pnr));
    }


}
