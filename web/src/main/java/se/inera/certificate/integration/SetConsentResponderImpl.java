package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.integration.util.ResultOfCallUtil;
import se.inera.certificate.service.ConsentService;
import se.inera.ifv.insuranceprocess.healthreporting.setconsent.v1.rivtabp20.SetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;

@Transactional
@SchemaValidation
public class SetConsentResponderImpl implements SetConsentResponderInterface {

    @Autowired
    private ConsentService consentService;

    @Override
    public SetConsentResponseType setConsent(AttributedURIType logicalAddress, SetConsentRequestType parameters) {
        consentService.setConsent(parameters.getPersonnummer(), parameters.isConsentGiven());
        SetConsentResponseType response = new SetConsentResponseType();
        response.setResult(ResultOfCallUtil.okResult());
        return response;
    }

}
