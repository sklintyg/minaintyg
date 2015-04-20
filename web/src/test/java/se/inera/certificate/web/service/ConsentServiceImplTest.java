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

import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;
import se.inera.intyg.insuranceprocess.healthreporting.getconsent.rivtabp20.v1.GetConsentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType;
import se.inera.intyg.insuranceprocess.healthreporting.setconsent.rivtabp20.v1.SetConsentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;


@RunWith(MockitoJUnitRunner.class)
public class ConsentServiceImplTest {

    @Mock
    private GetConsentResponderInterface getConsent = mock(GetConsentResponderInterface.class);

    @Mock
    private SetConsentResponderInterface setConsent = mock(SetConsentResponderInterface.class);

    @InjectMocks
    private ConsentService service = new ConsentServiceImpl();

    @Test
    public void testFetchConsentGiven() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        consentResponse.setConsentGiven(true);
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.OK);
        consentResponse.setResult(resultOfCall);
        
        when(getConsent.getConsent(Mockito.any(AttributedURIType.class), Mockito.any(GetConsentRequestType.class))).thenReturn(consentResponse);
        boolean result = service.fetchConsent("1234567890");
        assertTrue(result);

    }

    @Test
    public void testFetchConsentNotGiven() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        consentResponse.setConsentGiven(false);
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.OK);
        consentResponse.setResult(resultOfCall);
        
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

    @Test(expected = RuntimeException.class)
    public void testGetConsentWhenErrorResponseCode() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.ERROR);
        consentResponse.setResult(resultOfCall);
        consentResponse.setConsentGiven(false);

        when(getConsent.getConsent(Mockito.any(AttributedURIType.class), Mockito.any(GetConsentRequestType.class))).thenReturn(consentResponse);
        service.fetchConsent("1234567890");

    }

}
