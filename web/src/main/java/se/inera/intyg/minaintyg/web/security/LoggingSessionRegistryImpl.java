/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.web.service.MonitoringLogService;
import se.inera.intyg.schemas.contract.Personnummer;

@Service
public class LoggingSessionRegistryImpl extends SessionRegistryImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingSessionRegistryImpl.class);

    @Autowired
    private MonitoringLogService monitoringService;

    @Override
    public void registerNewSession(String sessionId, Object principal) {

        LOGGER.debug("Attempting to register new session '{}'", sessionId);

        if (principal != null && principal instanceof Citizen) {
            Citizen user = (Citizen) principal;
            monitoringService.logCitizenLogin(createPnr(user.getUsername()),
                user.getLoginMethod() != null ? user.getLoginMethod().name() : null);
        }
        super.registerNewSession(sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId) {

        LOGGER.debug("Attempting to remove session '{}'", sessionId);

        SessionInformation sessionInformation = getSessionInformation(sessionId);

        if (sessionInformation == null) {
            super.removeSessionInformation(sessionId);
            return;
        }

        Object principal = sessionInformation.getPrincipal();

        if (principal instanceof Citizen) {
            Citizen user = (Citizen) principal;
            monitoringService.logCitizenLogout(createPnr(user.getUsername()),
                user.getLoginMethod() != null ? user.getLoginMethod().name() : null);
        }

        super.removeSessionInformation(sessionId);
    }

    private Personnummer createPnr(String personId) {
        return Personnummer.createPersonnummer(personId).orElse(null);
    }

}
