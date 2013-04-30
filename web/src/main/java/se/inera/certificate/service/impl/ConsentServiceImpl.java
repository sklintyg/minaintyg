/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.service.ConsentService;

/**
 * @author andreaskaltenbach
 */
@Service
@Transactional
public class ConsentServiceImpl implements ConsentService {

    private Map<String, Boolean> consents = new HashMap<>();

    @Override
    public boolean isConsent(String personnummer) {
        return Boolean.TRUE.equals(consents.get(personnummer));
    }

    @Override
    public void setConsent(String personnummer, boolean consentGiven) {
        if (consentGiven) {
            consents.put(personnummer, Boolean.TRUE);
        } else {
            consents.remove(personnummer);
        }
    }

}
