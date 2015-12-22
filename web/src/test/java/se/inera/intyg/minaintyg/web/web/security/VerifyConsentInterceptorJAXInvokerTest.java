/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.web.web.security;

import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VerifyConsentInterceptorJAXInvokerTest {

    @Mock
    private CitizenService service = mock(CitizenService.class);

    @InjectMocks
    private VerifyConsentJAXRSInvoker invoker = new VerifyConsentJAXRSInvoker();

    @Test
    public void testPrehandleNoConsentJson() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        Object preHandle = invoker.invoke(null,null,null);
        assertNotNull(preHandle);
        MessageContentsList messageContentsList = (MessageContentsList) preHandle;
        javax.ws.rs.core.Response wsResponse = (javax.ws.rs.core.Response) messageContentsList.get(0);

        final Response.StatusType statusInfo = wsResponse.getStatusInfo();
        assertEquals(statusInfo.getStatusCode(), Response.Status.FORBIDDEN.getStatusCode());

    }

    @Test
    public void testPrehandleDoesNothingWhenConsentGiven() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK);
        citizen.setConsent(true);
        when(service.getCitizen()).thenReturn(citizen);

        final Long consentCounter = invoker.getConsentCounter();
        try{
            invoker.invoke(null,null,null);
        } catch (Exception ex) {
            assertFalse(false);
        }

        assertTrue((consentCounter+1) == invoker.getConsentCounter());

    }

}
