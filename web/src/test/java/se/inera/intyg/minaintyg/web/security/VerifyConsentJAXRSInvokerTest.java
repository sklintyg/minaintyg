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
package se.inera.intyg.minaintyg.web.security;

import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.MessageContentsList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.minaintyg.web.controller.appconfig.ConfigApiController;
import se.inera.intyg.minaintyg.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.service.ConsentService;
import se.inera.intyg.schemas.contract.Personnummer;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerifyConsentJAXRSInvokerTest {

    private static final String PERSON_FULL_NAME = "Tolvan Tolvansson";

    @Mock
    private CitizenService service;

    @Mock
    private ConsentService consentService;

    @InjectMocks
    private VerifyConsentJAXRSInvoker invoker;

    @Test
    public void testPrehandleNoConsentJson() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK, PERSON_FULL_NAME, false);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);

        MessageContentsList res = (MessageContentsList) invoker.invoke(null, null, null);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), ((Response) res.get(0)).getStatus());
    }

    @Test
    public void testPrehandleNoConsentMethodAllowedWithoutConsent() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK, PERSON_FULL_NAME, false);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);
        Exchange exchange = mock(Exchange.class);
        OperationResourceInfo ori = mock(OperationResourceInfo.class);
        when(ori.getMethodToInvoke()).thenReturn(ConfigApiController.class.getMethod("getModulesMap", new Class[] {}));
        when(exchange.get(OperationResourceInfo.class)).thenReturn(ori);

        final Long consentCounter = invoker.getConsentCounter();
        try{
            invoker.invoke(exchange, null, null);
        } catch (Exception ex) {
        }

        assertTrue((consentCounter+1) == invoker.getConsentCounter());
    }

    @Test
    public void testPrehandleDoesNothingWhenConsentGiven() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK, PERSON_FULL_NAME, false);
        citizen.setConsent(true);
        when(service.getCitizen()).thenReturn(citizen);

        final Long consentCounter = invoker.getConsentCounter();
        try{
            invoker.invoke(null,null,null);
        } catch (Exception ex) {
        }

        assertTrue((consentCounter+1) == invoker.getConsentCounter());
    }

    @Test
    public void testInvokeNoCitizen() throws Exception {
        when(service.getCitizen()).thenReturn(null);

        final Long consentCounter = invoker.getConsentCounter();
        MessageContentsList res = (MessageContentsList) invoker.invoke(null,null,null);

        assertNotNull(res);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), ((Response) res.get(0)).getStatus());
        assertEquals(consentCounter, invoker.getConsentCounter());
    }

    @Test
    public void testInvokeConsentNotKnown() throws Exception {
        final String personId = "19121212-1212";
        final Personnummer pnr = new Personnummer(personId);
        Citizen citizen = new CitizenImpl(personId, LoginMethodEnum.MVK, PERSON_FULL_NAME, false);
        when(service.getCitizen()).thenReturn(citizen);
        when(consentService.fetchConsent(pnr)).thenReturn(true);

        final Long consentCounter = invoker.getConsentCounter();

        try{
            invoker.invoke(null, null, null);
        } catch (Exception ex) {
        }

        assertEquals((Long) (consentCounter+1), invoker.getConsentCounter());
        verify(consentService).fetchConsent(pnr);
        assertTrue(citizen.hasConsent());
    }
}
