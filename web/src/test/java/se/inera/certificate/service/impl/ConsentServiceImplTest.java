package se.inera.certificate.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.dao.ConsentDao;
import se.inera.certificate.service.ConsentService;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConsentServiceImplTest {

    @Mock
    private ConsentDao consentDao = mock(ConsentDao.class);

    @InjectMocks
    private ConsentService consentService = new ConsentServiceImpl();

    @Test
    public void unknownUserHasNoConsent() {

        when(consentDao.hasConsent("unknown")).thenReturn(false);

        assertFalse(consentService.isConsent("unknown"));

        verify(consentDao).hasConsent("unknown");
    }

    @Test
    public void testSettingConsent() {
        consentService.setConsent("consentUser", true);
        verify(consentDao).setConsent("consentUser");
    }

    @Test
    public void testSettingNoConsent() {
        consentService.setConsent("consentUser", false);
        verify(consentDao).revokeConsent("consentUser");
    }
}
