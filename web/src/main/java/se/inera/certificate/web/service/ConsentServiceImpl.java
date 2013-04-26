/**
 * Copyright (C) 2012 Inera AB (http://www.inera.se)
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

/**
 */
@Service
public class ConsentServiceImpl implements ConsentService {

    private static final Logger log = LoggerFactory.getLogger(ConsentServiceImpl.class);

    @Autowired
    private GetConsentResponderInterface getConsent;

    @Override
    public boolean fetchConsent(String username) {
        log.debug("About to fetch consent...");
        GetConsentRequestType parameters = new GetConsentRequestType();
        parameters.setPersonnummer(username);

        GetConsentResponseType consent = getConsent.getConsent(null, parameters); 
        boolean consentResult = consent.isConsentGiven();
        log.debug("Consent result is {}", consentResult);
        return consentResult;
    }
}
