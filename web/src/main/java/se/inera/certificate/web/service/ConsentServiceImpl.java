/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.inera.ifv.insuranceprocess.healthreporting.getconsent.v1.rivtabp20.GetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.setconsent.v1.rivtabp20.SetConsentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

/**
 */
@Service
public class ConsentServiceImpl implements ConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsentServiceImpl.class);

    @Autowired
    private GetConsentResponderInterface getConsent;

    @Autowired
    private SetConsentResponderInterface setConsent;

    @Override
    public boolean fetchConsent(String username) {
        LOG.debug("About to fetch consent...");
        GetConsentRequestType parameters = new GetConsentRequestType();
        parameters.setPersonnummer(username);

        GetConsentResponseType consent = getConsent.getConsent(null, parameters);
        //When resultcode is error we cannot trust what the isConsentGiven says, since always have a value...
        if (consent.getResult().getResultCode() == ResultCodeEnum.ERROR) {
            throw new IllegalStateException("Cant determine consentStatus - ERROR result from GetConsentResponderInterface: " + consent.getResult().getErrorText());
        }
        boolean consentResult = consent.isConsentGiven();
        LOG.debug("Consent result is {}", consentResult);
        return consentResult;
    }

    @Override
    public boolean setConsent(String username, boolean consent) {
        LOG.debug("About to set consent...");
        SetConsentRequestType parameters = new SetConsentRequestType();
        parameters.setPersonnummer(username);
        parameters.setConsentGiven(consent);
        SetConsentResponseType consentResponse = setConsent.setConsent(null, parameters);
        ResultCodeEnum result = consentResponse.getResult().getResultCode();
        LOG.debug("resultcode is {}", result);
        return !consentResponse.getResult().getResultCode().equals(ResultCodeEnum.ERROR);

    }
}
