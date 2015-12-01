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
