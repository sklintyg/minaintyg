package se.inera.certificate.web.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.getconsent.v1.rivtabp20.GetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.setconsent.v1.rivtabp20.SetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

@RunWith(MockitoJUnitRunner.class)
public class ConsentServiceImplTest {

    @Mock
    GetConsentResponderInterface getConsent = mock(GetConsentResponderInterface.class);

    @Mock
    SetConsentResponderInterface setConsent = mock(SetConsentResponderInterface.class);

    @InjectMocks
    private ConsentService service = new ConsentServiceImpl();

    @Test
    public void testFetchConsentGiven() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        consentResponse.setConsentGiven(true);

        when(getConsent.getConsent(Mockito.any(AttributedURIType.class), Mockito.any(GetConsentRequestType.class))).thenReturn(consentResponse);
        boolean result = service.fetchConsent("1234567890");
        assertTrue(result);

    }

    @Test
    public void testFetchConsentNotGiven() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        consentResponse.setConsentGiven(false);

        when(getConsent.getConsent(Mockito.any(AttributedURIType.class), Mockito.any(GetConsentRequestType.class))).thenReturn(consentResponse);
        boolean result = service.fetchConsent("1234567890");
        assertFalse(result);

    }

    @Test
    public void testSetConsentSuccess() {
        SetConsentResponseType consentResponse = new SetConsentResponseType();
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.OK);
        consentResponse.setResult(resultOfCall);

        when(setConsent.setConsent(Mockito.any(AttributedURIType.class), Mockito.any(SetConsentRequestType.class))).thenReturn(consentResponse);
        boolean result = service.setConsent("1234567890", true);
        assertTrue(result);

    }

    @Test
    public void testSetConsentError() {
        SetConsentResponseType consentResponse = new SetConsentResponseType();
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.ERROR);
        consentResponse.setResult(resultOfCall);

        when(setConsent.setConsent(Mockito.any(AttributedURIType.class), Mockito.any(SetConsentRequestType.class))).thenReturn(consentResponse);
        boolean result = service.setConsent("1234567890", true);
        assertFalse(result);

    }

}
