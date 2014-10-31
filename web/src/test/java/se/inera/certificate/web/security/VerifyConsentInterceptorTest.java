package se.inera.certificate.web.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.web.service.CitizenService;

@RunWith(MockitoJUnitRunner.class)
public class VerifyConsentInterceptorTest {

    @Mock
    private CitizenService service = mock(CitizenService.class);

    @InjectMocks
    private VerifyConsentInterceptor interceptor = new VerifyConsentInterceptor();


    @Test
    public void testPrehandleNoConsentNoJson() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        boolean preHandle = interceptor.preHandle(null, response, null);
        assertFalse(preHandle);
        verify(response).sendRedirect(Mockito.anyString());

    }

    @Test
    public void testPrehandleDoesNothingWhenConsentGiven() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK);
        citizen.setConsent(true);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        boolean preHandle = interceptor.preHandle(null, response, null);
        assertTrue(preHandle);
        Mockito.verifyZeroInteractions(response);

    }

}
