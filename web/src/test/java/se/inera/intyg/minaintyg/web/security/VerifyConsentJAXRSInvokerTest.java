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
package se.inera.intyg.minaintyg.web.security;

import javax.ws.rs.core.Response;

import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.MessageContentsList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import se.inera.intyg.minaintyg.web.service.CitizenService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerifyConsentJAXRSInvokerTest {

    private static final String PERSON_FULL_NAME = "Tolvan Tolvansson";
    private static final String PNR_TOLVAN = "19121212-1212";
    private static final String PNR_OTHER = "20121212-1212";


    @Mock
    private CitizenService service;

    @InjectMocks
    private VerifyConsentJAXRSInvoker invoker;

    @Test
    public void testPrehandleNoConsentJson() {
        Citizen citizen = new CitizenImpl(PNR_TOLVAN, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);

        final Long consentCounter = invoker.getConsentCounter();

        try{
            invoker.invoke(null, null, null);
        } catch (Exception ex) {
        }

        assertEquals((Long) (consentCounter+1), invoker.getConsentCounter());
    }

    @Test
    public void testPrehandleNoConsentMethodAllowedWithoutConsent() {
        Citizen citizen = new CitizenImpl(PNR_TOLVAN, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);
        Exchange exchange = mock(Exchange.class);

        final Long consentCounter = invoker.getConsentCounter();
        try{
            invoker.invoke(exchange, null, null);
        } catch (Exception ex) {
        }

        assertTrue((consentCounter+1) == invoker.getConsentCounter());
    }

    @Test
    public void testPrehandleDoesNothingWhenConsentGiven() {
        Citizen citizen = new CitizenImpl(PNR_TOLVAN, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
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
    public void testInvokeNoCitizen() {
        when(service.getCitizen()).thenReturn(null);

        final Long consentCounter = invoker.getConsentCounter();
        MessageContentsList res = (MessageContentsList) invoker.invoke(null,null,null);

        assertNotNull(res);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), ((Response) res.get(0)).getStatus());
        assertEquals(consentCounter, invoker.getConsentCounter());
    }

    @Test
    public void testInvokeConsentUnknown() {
        Citizen citizen = new CitizenImpl(PNR_OTHER, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        when(service.getCitizen()).thenReturn(citizen);

        final Long consentCounter = invoker.getConsentCounter();

        try{
            invoker.invoke(null, null, null);
        } catch (Exception ex) {
        }

        assertEquals((Long) (consentCounter+1), invoker.getConsentCounter());
    }
}
