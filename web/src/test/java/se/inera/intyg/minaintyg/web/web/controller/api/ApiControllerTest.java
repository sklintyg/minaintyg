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
package se.inera.intyg.minaintyg.web.web.controller.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.api.ConsentResponse;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.api.UserInfo;
import se.inera.intyg.minaintyg.web.web.security.BrowserClosedInterceptor;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.security.LoginMethodEnum;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.web.service.ConsentService;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {

    private static final String CIVIC_REGISTRATION_NUMBER = "19121212-1212";
    private static final Personnummer PNR = new Personnummer(CIVIC_REGISTRATION_NUMBER);
    private static final String FKASSA_RECIPIENT_ID = "FKASSA";
    private static final String TRANSP_RECIPIENT_ID = "TRANSP";
    private static final LoginMethodEnum LOGIN_METHOD = LoginMethodEnum.MVK;
    private static final String PERSON_FULLNAME = "Tolvan Tolvansson";

    @Mock
    private CertificateService certificateService;

    @Mock
    private ConsentService consentService;

    @Mock
    private CitizenService citizenService;

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @InjectMocks
    private ApiController apiController;

    @Test
    public void testListCertificates() throws Exception {
        when(certificateService.getCertificates(PNR, false)).thenReturn(Arrays.asList(mock(UtlatandeMetaData.class)));
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        List<CertificateMeta> res = apiController.listCertificates();

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(certificateService).getCertificates(PNR, false);
    }

    @Test
    public void testListArchivedCertificates() throws Exception {
        when(certificateService.getCertificates(PNR, true)).thenReturn(Arrays.asList(mock(UtlatandeMetaData.class)));
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        List<CertificateMeta> res = apiController.listArchivedCertificates();

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(certificateService).getCertificates(PNR, true);
    }

    @Test
    public void testRecipients() throws Exception {
        final String type = "fk7263";
        when(certificateService.getRecipientsForCertificate(type)).thenReturn(Arrays.asList(mock(UtlatandeRecipient.class)));
        List<UtlatandeRecipient> res = apiController.listRecipients(type);

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(certificateService).getRecipientsForCertificate(type);
    }

    @Test
    public void testArchive() throws Exception {
        final String id = "intyg-id";
        when(certificateService.archiveCertificate(id, PNR)).thenReturn(mock(UtlatandeMetaData.class));
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        CertificateMeta res = apiController.archive(id);

        assertNotNull(res);

        verify(certificateService).archiveCertificate(id, PNR);
    }

    @Test
    public void testRestore() throws Exception {
        final String id = "intyg-id";
        when(certificateService.restoreCertificate(id, PNR)).thenReturn(mock(UtlatandeMetaData.class));
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        CertificateMeta res = apiController.restore(id);

        assertNotNull(res);

        verify(certificateService).restoreCertificate(id, PNR);
    }

    @Test
    public void testGiveConsent() throws Exception {
        when(consentService.setConsent(PNR)).thenReturn(true);
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        ConsentResponse res = apiController.giveConsent();

        assertNotNull(res);
        assertTrue(res.getResult());

        verify(citizenService.getCitizen()).setConsent(true); // verify user is updated
        verify(consentService).setConsent(PNR);
    }

    @Test
    public void testGiveConsentFailed() throws Exception {
        when(consentService.setConsent(PNR)).thenReturn(false);
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        ConsentResponse res = apiController.giveConsent();

        assertNotNull(res);
        assertTrue(res.getResult());

        verify(citizenService.getCitizen()).setConsent(false); // verify user is updated
        verify(consentService).setConsent(PNR);
    }

    @Test
    public void testRevokeConsent() throws Exception {
        when(consentService.revokeConsent(PNR)).thenReturn(true);
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        ConsentResponse res = apiController.revokeConsent();

        assertNotNull(res);
        assertTrue(res.getResult());

        verify(citizenService.getCitizen()).setConsent(false); // verify user is updated
        verify(consentService).revokeConsent(PNR);
    }

    @Test
    public void testRevokeFailed() throws Exception {
        when(consentService.revokeConsent(PNR)).thenReturn(false);
        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        ConsentResponse res = apiController.revokeConsent();

        assertNotNull(res);
        assertFalse(res.getResult());

        verify(citizenService.getCitizen(), never()).setConsent(false); // verify user is never updated
        verify(consentService).revokeConsent(PNR);
    }

    @Test
    public void testOnBeforeUnload() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getSession()).thenReturn(mock(HttpSession.class));
        when(req.getSession().getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP)).thenReturn(null);
        String res = apiController.onbeforeunload(req);

        assertEquals("ok", res);
        verify(req.getSession()).getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP);
        ArgumentCaptor<LocalDateTime> valueCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(req.getSession()).setAttribute(eq(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP), valueCaptor.capture());
        assertNotNull(valueCaptor.getValue());
    }

    @Test
    public void testOnBeforeUnloadAlreadySet() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getSession()).thenReturn(mock(HttpSession.class));
        when(req.getSession().getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP)).thenReturn(LocalDateTime.now());
        String res = apiController.onbeforeunload(req);

        assertEquals("ok", res);
        verify(req.getSession()).getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP);
        verify(req.getSession(), never()).setAttribute(eq(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP), anyObject()); // verify
                                                                                                                            // not
                                                                                                                            // overridden
    }

    @Test
    public void testGetQuestions() throws Exception {
        final String type = "luse";
        final String version = "1.0";
        final String questionsString = "{questions}";
        when(certificateService.getQuestions(type, version)).thenReturn(questionsString);
        String res = apiController.getQuestions(type, version);

        assertEquals(questionsString, res);
        verify(certificateService).getQuestions(type, version);
    }

    @Test
    public void testSendCertificate() throws Exception {

        // When
        Personnummer personNummer = new Personnummer(CIVIC_REGISTRATION_NUMBER);
        String certificateId = "abc-123";
        List<String> recipients = Arrays.asList(FKASSA_RECIPIENT_ID, TRANSP_RECIPIENT_ID);
        List<SendToRecipientResult> expectedResponse = new ArrayList<>();
        expectedResponse.add(new SendToRecipientResult(FKASSA_RECIPIENT_ID, true, LocalDateTime.now()));
        expectedResponse.add(new SendToRecipientResult(TRANSP_RECIPIENT_ID, true, LocalDateTime.now()));

        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        when(certificateService.sendCertificate(personNummer, certificateId, recipients)).thenReturn(expectedResponse);

        // Then
        final List<SendToRecipientResult> actualResult = apiController.send(certificateId, recipients);

        // Verify
        assertEquals(actualResult, expectedResponse);
        verify(certificateService).sendCertificate(eq(personNummer), eq(certificateId), eq(recipients));
    }

    @Test
    public void testGetUser() throws Exception {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(CIVIC_REGISTRATION_NUMBER);
        when(citizen.getLoginMethod()).thenReturn(LOGIN_METHOD);
        when(citizen.getFullName()).thenReturn(PERSON_FULLNAME);
        when(citizen.isSekretessmarkering()).thenReturn(true);
        when(citizen.hasConsent()).thenReturn(true);

        when(citizenService.getCitizen()).thenReturn(citizen);

        UserInfo user = apiController.getUser();

        assertNotNull(user);
        assertEquals(CIVIC_REGISTRATION_NUMBER, user.getPersonId());
        assertEquals(LOGIN_METHOD.name(), user.getLoginMethod());
        assertEquals(PERSON_FULLNAME, user.getFullName());
        assertEquals(true, user.isSekretessmarkering());
        assertEquals(true, user.isConsentGiven());


    }

    private void mockCitizen(String personId) {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(personId);
        when(citizenService.getCitizen()).thenReturn(citizen);
    }

}
