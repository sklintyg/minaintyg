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

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.insuranceprocess.healthreporting.getconsent.rivtabp20.v1.GetConsentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType;
import se.inera.intyg.insuranceprocess.healthreporting.setconsent.rivtabp20.v1.SetConsentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType;


@Service
public class ConsentServiceImpl implements ConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsentServiceImpl.class);

    @Autowired
    private GetConsentResponderInterface getConsent;

    @Autowired
    private SetConsentResponderInterface setConsent;

    @Autowired
    private MonitoringLogService monitoringService;

    @Override
    public boolean fetchConsent(Personnummer civicRegistrationNumber) {
        LOG.debug("About to fetch consent...");
        GetConsentRequestType parameters = new GetConsentRequestType();
        parameters.setPersonnummer(civicRegistrationNumber.getPersonnummer());

        GetConsentResponseType consent = getConsent.getConsent(null, parameters);
        // When resultcode is error we cannot trust what the isConsentGiven says, since always have a value...
        if (consent.getResult().getResultCode() == ResultCodeEnum.ERROR) {
            throw new IllegalStateException("Cant determine consentStatus - ERROR result from GetConsentResponderInterface: "
                    + consent.getResult().getErrorText());
        }
        boolean consentResult = consent.isConsentGiven();
        LOG.debug("Consent result is {}", consentResult);
        return consentResult;
    }

    @Override
    public boolean setConsent(Personnummer civicRegistrationNumber) {
        LOG.debug("About to set consent...");
        boolean consentResult = setConsent(civicRegistrationNumber, true);
        monitoringService.logCitizenConsentGiven(civicRegistrationNumber);
        return consentResult;
    }

    @Override
    public boolean revokeConsent(Personnummer civicRegistrationNumber) {
        LOG.debug("About to revoke consent...");
        boolean consentResult = setConsent(civicRegistrationNumber, false);
        monitoringService.logCitizenConsentRevoked(civicRegistrationNumber);
        return consentResult;
    }

    private boolean setConsent(Personnummer civicRegistrationNumber, boolean consent) {
        SetConsentRequestType parameters = new SetConsentRequestType();
        parameters.setPersonnummer(civicRegistrationNumber.getPersonnummer());
        parameters.setConsentGiven(consent);
        SetConsentResponseType consentResponse = setConsent.setConsent(null, parameters);
        ResultCodeEnum result = consentResponse.getResult().getResultCode();
        LOG.debug("resultcode is {}", result);
        return !consentResponse.getResult().getResultCode().equals(ResultCodeEnum.ERROR);

    }
}
