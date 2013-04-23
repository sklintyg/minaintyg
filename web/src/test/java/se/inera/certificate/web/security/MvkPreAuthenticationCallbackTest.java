package se.inera.certificate.web.security;

import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.callistasoftware.netcare.mvk.authentication.service.api.impl.AuthenticationResultImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class MvkPreAuthenticationCallbackTest {
    @Test
    public void testLookupPrincipal() throws Exception {

        AuthenticationResult authenticationResultMock = mock(AuthenticationResult.class);
        when(authenticationResultMock.getUsername()).thenReturn("1234567890");

        MvkPreAuthenticationCallback mvkPreAuthenticationCallback = new MvkPreAuthenticationCallback();
        UserDetails userDetails = mvkPreAuthenticationCallback.lookupPrincipal(authenticationResultMock);

        assertEquals(userDetails.getClass().getCanonicalName(), CitizenImpl.class.getCanonicalName());
        assertEquals(((Citizen) userDetails).getUsername(), "1234567890");

    }
}
