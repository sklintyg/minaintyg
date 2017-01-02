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
package se.inera.intyg.minaintyg.web.web.security.saml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.opensaml.ws.transport.InTransport;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.springframework.security.saml.context.SAMLMessageContext;

@RunWith(MockitoJUnitRunner.class)
public class SAMLContextProviderLBTest {

    private SAMLContextProviderLB samlContextProviderLB = new SAMLContextProviderLB();

    @Test
    public void testPopulateGenericContextUsesLpRequestWrapper() throws Exception {
        final String contextPath = "/contextPath";
        final String scheme = "https";
        final String serverName = "serverName";
        final int serverPort = 123;
        final String servletPath = "/servletPath";
        final String pathInfo = "/pathinfo";
        samlContextProviderLB.setContextPath(contextPath);
        samlContextProviderLB.setScheme(scheme);
        samlContextProviderLB.setServerName(serverName);
        samlContextProviderLB.setServerPort(serverPort);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn(servletPath);
        when(request.getPathInfo()).thenReturn(pathInfo);
        SAMLMessageContext context = mock(SAMLMessageContext.class);

        samlContextProviderLB.populateGenericContext(request, mock(HttpServletResponse.class), context);

        ArgumentCaptor<InTransport> inTransportCaptor = ArgumentCaptor.forClass(InTransport.class);
        verify(context).setInboundMessageTransport(inTransportCaptor.capture());

        HttpServletRequest wrappedRequest = ((HttpServletRequestAdapter) inTransportCaptor.getValue()).getWrappedRequest();
        assertEquals(contextPath, wrappedRequest.getContextPath());
        assertEquals(scheme, wrappedRequest.getScheme());
        assertEquals(serverName, wrappedRequest.getServerName());
        assertEquals(serverPort, wrappedRequest.getServerPort());
        assertEquals(contextPath + servletPath, wrappedRequest.getRequestURI());
        assertEquals(scheme + "://" + serverName + contextPath + servletPath + pathInfo, wrappedRequest.getRequestURL().toString());
        assertTrue(wrappedRequest.isSecure());
    }

    @Test
    public void testPopulateGenericContextUsesLpRequestWrapperWithoutPathInfo() throws Exception {
        final String contextPath = "/contextPath";
        final String scheme = "https";
        final String serverName = "serverName";
        final int serverPort = 123;
        final String servletPath = "/servletPath";
        samlContextProviderLB.setContextPath(contextPath);
        samlContextProviderLB.setScheme(scheme);
        samlContextProviderLB.setServerName(serverName);
        samlContextProviderLB.setServerPort(serverPort);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn(servletPath);
        SAMLMessageContext context = mock(SAMLMessageContext.class);

        samlContextProviderLB.populateGenericContext(request, mock(HttpServletResponse.class), context);

        ArgumentCaptor<InTransport> inTransportCaptor = ArgumentCaptor.forClass(InTransport.class);
        verify(context).setInboundMessageTransport(inTransportCaptor.capture());

        HttpServletRequest wrappedRequest = ((HttpServletRequestAdapter) inTransportCaptor.getValue()).getWrappedRequest();
        assertEquals(contextPath + servletPath, wrappedRequest.getRequestURI());
        assertEquals(scheme + "://" + serverName + contextPath + servletPath, wrappedRequest.getRequestURL().toString());
    }

    @Test
    public void testPopulateGenericContextUsesLpRequestWrapperIncludeServerPortInRequestUrl() throws Exception {
        final String contextPath = "/contextPath";
        final String scheme = "https";
        final String serverName = "serverName";
        final int serverPort = 123;
        final String servletPath = "/servletPath";
        samlContextProviderLB.setContextPath(contextPath);
        samlContextProviderLB.setScheme(scheme);
        samlContextProviderLB.setServerName(serverName);
        samlContextProviderLB.setServerPort(serverPort);
        samlContextProviderLB.setIncludeServerPortInRequestURL(true);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn(servletPath);
        SAMLMessageContext context = mock(SAMLMessageContext.class);

        samlContextProviderLB.populateGenericContext(request, mock(HttpServletResponse.class), context);

        ArgumentCaptor<InTransport> inTransportCaptor = ArgumentCaptor.forClass(InTransport.class);
        verify(context).setInboundMessageTransport(inTransportCaptor.capture());

        HttpServletRequest wrappedRequest = ((HttpServletRequestAdapter) inTransportCaptor.getValue()).getWrappedRequest();
        assertEquals(contextPath + servletPath, wrappedRequest.getRequestURI());
        assertEquals(scheme + "://" + serverName + ":" + serverPort + contextPath + servletPath, wrappedRequest.getRequestURL().toString());
    }

    @Test
    public void testPopulateGenericContextUsesLpRequestWrapperNotSecure() throws Exception {
        final String scheme = "http";
        samlContextProviderLB.setScheme(scheme);
        SAMLMessageContext context = mock(SAMLMessageContext.class);

        samlContextProviderLB.populateGenericContext(mock(HttpServletRequest.class), mock(HttpServletResponse.class), context);

        ArgumentCaptor<InTransport> inTransportCaptor = ArgumentCaptor.forClass(InTransport.class);
        verify(context).setInboundMessageTransport(inTransportCaptor.capture());

        HttpServletRequest wrappedRequest = ((HttpServletRequestAdapter) inTransportCaptor.getValue()).getWrappedRequest();
        assertEquals(scheme, wrappedRequest.getScheme());
        assertFalse(wrappedRequest.isSecure());
    }
}
