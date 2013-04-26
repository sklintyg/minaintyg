package se.inera.certificate.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.service.ConsentService;
import se.inera.ifv.insuranceprocess.healthreporting.setconsent.v1.rivtabp20.SetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

@RunWith(MockitoJUnitRunner.class)
public class SetConsentResponderImplTest {

    @Mock
    private ConsentService consentService = mock(ConsentService.class);

    @InjectMocks
    private SetConsentResponderInterface responder = new SetConsentResponderImpl();

    @Test
    public void consentServiceIsCalledWithPersonnummerAndConsentGiven() {
        responder.setConsent(null, createRequest("12345678-1234", false));
        responder.setConsent(null, createRequest("12345678-1234", true));
        verify(consentService).setConsent("12345678-1234", false);
        verify(consentService).setConsent("12345678-1234", true);
    }

    @Test
    public void consentServiceReturnsOK() {
        SetConsentResponseType result = responder.setConsent(null, createRequest("12345678-1234", false));
        assertEquals(ResultCodeEnum.OK, result.getResult().getResultCode());
    }

    private SetConsentRequestType createRequest(String id, boolean consentGiven) {
        SetConsentRequestType parameters = new SetConsentRequestType();
        parameters.setPersonnummer(id);
        parameters.setConsentGiven(consentGiven);
        return parameters;
    }

}
