package se.inera.certificate.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.inera.certificate.service.ConsentService;

public class ConsentServiceImplTest {

    private ConsentService consentService = new ConsentServiceImpl();
    @Test
    public void unknownUserHasNoConsent() {
        assertFalse(consentService.isConsent("unknown"));
    }

    @Test
    public void afterSettingConsentUserHasConsent() {
        consentService.setConsent("consentUser", true);
        assertTrue(consentService.isConsent("consentUser"));
    }

    @Test
    public void afterSettingNoConsentUserHasNoConsent() {
        consentService.setConsent("consentUser", false);
        assertFalse(consentService.isConsent("consentUser"));
    }

    @Test
    public void userWithConsentCanRemoveConsent() {
        consentService.setConsent("consentUser", true);
        assertTrue(consentService.isConsent("consentUser"));
        consentService.setConsent("consentUser", false);
        assertFalse(consentService.isConsent("consentUser"));
    }
}
