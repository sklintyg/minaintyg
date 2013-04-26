package se.inera.certificate.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.service.ConsentService;
import se.inera.ifv.insuranceprocess.healthreporting.getconsent.v1.rivtabp20.GetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType;

public class GetConsentResponderImpl implements GetConsentResponderInterface {

    @Autowired
    private ConsentService consentService;

    @Override
    public GetConsentResponseType getConsent(AttributedURIType logicalAddress, GetConsentRequestType parameters) {
        GetConsentResponseType response = new GetConsentResponseType();
        response.setConsentGiven(consentService.isConsent(parameters.getPersonnummer()));
        response.setResult(ResultOfCallUtil.okResult());
        return response;
    }

}
