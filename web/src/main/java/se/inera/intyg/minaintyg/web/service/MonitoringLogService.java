/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import se.inera.intyg.schemas.contract.Personnummer;

public interface MonitoringLogService {

    void logCitizenLogin(Personnummer userId, String loginMethod);

    void logCitizenLogout(Personnummer userId, String loginMethod);

    void logCertificateRead(String id, String typ);

    void logCertificateSend(String certificateId, String recipientId, String certificateType);

    void logCertificateArchived(String certificateId);

    void logCertificateRestored(String certificateId);

    void logBrowserInfo(String browserName, String browserVersion, String osFamily, String osVersion, String width, String height);

    void logCertificatePrintedFully(String intygsId, String intygsType);

    void logCertificatePrintedEmployerCopy(String intygsId, String intygsType);

    // Saml
    void logSamlStatusForFailedLogin(String issuer, String samlStatus);

    void logOpenedAbout(String pnr);

    void logOpenedFAQ(String pnr);

    void logOpenedQuestion(String id, String title, String pnr);
}
