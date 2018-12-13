/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.api.UserInfo;
import se.inera.intyg.minaintyg.web.security.BrowserClosedInterceptor;
import se.inera.intyg.minaintyg.web.security.Citizen;
import se.inera.intyg.minaintyg.web.security.LoginMethodEnum;
import se.inera.intyg.minaintyg.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.schemas.contract.Personnummer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {

    private static final String CIVIC_REGISTRATION_NUMBER = "19121212-1212";
    private static final String FKASSA_RECIPIENT_ID = "FKASSA";
    private static final String TRANSP_RECIPIENT_ID = "TRANSP";
    private static final String PERSON_FULLNAME = "Tolvan Tolvansson";

    private static final Personnummer PNR = Personnummer.createPersonnummer(CIVIC_REGISTRATION_NUMBER).get();

    private static final LoginMethodEnum LOGIN_METHOD = LoginMethodEnum.ELVA77;

    @Mock
    private CertificateService certificateService;

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
        final String intygsId = UUID.randomUUID().toString();
        when(certificateService.getRecipientsForCertificate(intygsId)).thenReturn(Arrays.asList(mock(UtlatandeRecipient.class)));

        List<UtlatandeRecipient> res = apiController.listRecipients(intygsId);

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(certificateService).getRecipientsForCertificate(intygsId);
    }

    @Test
    public void testArchive() throws Exception {
        final String intygsId = UUID.randomUUID().toString();
        when(certificateService.archiveCertificate(intygsId, PNR)).thenReturn(mock(UtlatandeMetaData.class));
        mockCitizen(CIVIC_REGISTRATION_NUMBER);

        CertificateMeta res = apiController.archive(intygsId);

        assertNotNull(res);

        verify(certificateService).archiveCertificate(intygsId, PNR);
    }

    @Test
    public void testRestore() throws Exception {
        final String intygsId = UUID.randomUUID().toString();
        when(certificateService.restoreCertificate(intygsId, PNR)).thenReturn(mock(UtlatandeMetaData.class));
        mockCitizen(CIVIC_REGISTRATION_NUMBER);

        CertificateMeta res = apiController.restore(intygsId);

        assertNotNull(res);

        verify(certificateService).restoreCertificate(intygsId, PNR);
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
        verify(req.getSession(), never()).setAttribute(eq(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP), any()); // verify
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
        String certificateId = "abc-123";
        List<String> recipients = Arrays.asList(FKASSA_RECIPIENT_ID, TRANSP_RECIPIENT_ID);
        List<SendToRecipientResult> expectedResponse = new ArrayList<>();
        expectedResponse.add(new SendToRecipientResult(FKASSA_RECIPIENT_ID, true, LocalDateTime.now()));
        expectedResponse.add(new SendToRecipientResult(TRANSP_RECIPIENT_ID, true, LocalDateTime.now()));

        mockCitizen(CIVIC_REGISTRATION_NUMBER);
        when(certificateService.sendCertificate(PNR, certificateId, recipients)).thenReturn(expectedResponse);

        // Then
        final List<SendToRecipientResult> actualResult = apiController.send(certificateId, recipients);

        // Verify
        assertEquals(actualResult, expectedResponse);
        verify(certificateService).sendCertificate(eq(PNR), eq(certificateId), eq(recipients));
    }

    @Test
    public void testGetUser() throws Exception {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(CIVIC_REGISTRATION_NUMBER);
        when(citizen.getLoginMethod()).thenReturn(LOGIN_METHOD);
        when(citizen.getFullName()).thenReturn(PERSON_FULLNAME);
        when(citizen.isSekretessmarkering()).thenReturn(true);

        when(citizenService.getCitizen()).thenReturn(citizen);

        UserInfo user = apiController.getUser();

        assertNotNull(user);
        assertEquals(CIVIC_REGISTRATION_NUMBER, user.getPersonId());
        assertEquals(LOGIN_METHOD.name(), user.getLoginMethod());
        assertEquals(PERSON_FULLNAME, user.getFullName());
        assertTrue(user.isSekretessmarkering());
    }

    private void mockCitizen(String personId) {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(personId);
        when(citizenService.getCitizen()).thenReturn(citizen);
    }

}
