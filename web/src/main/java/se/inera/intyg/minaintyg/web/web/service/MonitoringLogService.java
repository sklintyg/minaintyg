/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.web.service;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

public interface MonitoringLogService {

    void logCitizenLogin(Personnummer userId, String loginMethod);

    void logCitizenLogout(Personnummer userId, String loginMethod);

    void logCitizenConsentGiven(Personnummer userId);

    void logCitizenConsentRevoked(Personnummer userId);

    void logCertificateRead(String id, String typ);

    void logCertificateSend(String certificateId, String recipientId);

    void logCertificateArchived(String certificateId);

    void logCertificateRestored(String certificateId);

}
