/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;
import se.inera.intyg.insuranceprocess.healthreporting.getconsent.rivtabp20.v1.GetConsentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType;
import se.inera.intyg.insuranceprocess.healthreporting.setconsent.rivtabp20.v1.SetConsentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;
import se.inera.intyg.schemas.contract.Personnummer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ConsentServiceImplTest {

    private final Personnummer pnr = Personnummer.createPersonnummer("20121212-1212").get();

    @Mock
    private GetConsentResponderInterface getConsent = mock(GetConsentResponderInterface.class);

    @Mock
    private SetConsentResponderInterface setConsent = mock(SetConsentResponderInterface.class);

    @Mock
    private MonitoringLogService monitoringServiceMock;

    @InjectMocks
    private ConsentService service = new ConsentServiceImpl();

    @Test
    public void testFetchConsentGiven() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        consentResponse.setConsentGiven(true);
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.OK);
        consentResponse.setResult(resultOfCall);

        when(getConsent.getConsent(
                or(isNull(), any(AttributedURIType.class)),
                or(isNull(), any(GetConsentRequestType.class)))
        ).thenReturn(consentResponse);

        boolean result = service.fetchConsent(pnr);
        assertTrue(result);
    }

    @Test
    public void testFetchConsentNotGiven() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        consentResponse.setConsentGiven(false);
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.OK);
        consentResponse.setResult(resultOfCall);

        when(getConsent.getConsent(
                or(isNull(), any(AttributedURIType.class)),
                or(isNull(), any(GetConsentRequestType.class)))
        ).thenReturn(consentResponse);

        boolean result = service.fetchConsent(pnr);
        assertFalse(result);

    }

    @Test
    public void testSetConsentSuccess() {
        SetConsentResponseType consentResponse = new SetConsentResponseType();
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.OK);
        consentResponse.setResult(resultOfCall);


        when(setConsent.setConsent(
                or(isNull(), any(AttributedURIType.class)),
                or(isNull(), any(SetConsentRequestType.class)))
        ).thenReturn(consentResponse);

        boolean result = service.setConsent(pnr);
        assertTrue(result);
        verify(monitoringServiceMock).logCitizenConsentGiven(pnr);
    }

    @Test
    public void testSetConsentError() {
        SetConsentResponseType consentResponse = new SetConsentResponseType();
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.ERROR);
        consentResponse.setResult(resultOfCall);

        when(setConsent.setConsent(
                or(isNull(), any(AttributedURIType.class)),
                or(isNull(), any(SetConsentRequestType.class)))
        ).thenReturn(consentResponse);

        boolean result = service.setConsent(pnr);
        assertFalse(result);

    }

    @Test(expected = RuntimeException.class)
    public void testGetConsentWhenErrorResponseCode() {
        GetConsentResponseType consentResponse = new GetConsentResponseType();
        ResultOfCall resultOfCall = new ResultOfCall();
        resultOfCall.setResultCode(ResultCodeEnum.ERROR);
        consentResponse.setResult(resultOfCall);
        consentResponse.setConsentGiven(false);

        when(getConsent.getConsent(
                or(isNull(), any(AttributedURIType.class)),
                or(isNull(), any(GetConsentRequestType.class)))
        ).thenReturn(consentResponse);

        service.fetchConsent(pnr);

    }

}