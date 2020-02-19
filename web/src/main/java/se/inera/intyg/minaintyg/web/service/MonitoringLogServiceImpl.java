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

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intyg.schemas.contract.Personnummer;

@Service
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private static final String SPACE = " ";

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringLogService.class);

    @Override
    public void logCitizenLogin(Personnummer userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGIN, Personnummer.getPersonnummerHashSafe(userId), loginMethod);
    }

    @Override
    public void logCitizenLogout(Personnummer userId, String loginMethod) {
        logEvent(MonitoringEvent.CITIZEN_LOGOUT, Personnummer.getPersonnummerHashSafe(userId), loginMethod);
    }

    @Override
    public void logCertificateRead(String certificateId, String certificateType) {
        logEvent(MonitoringEvent.CERTIFICATE_READ, certificateId, certificateType);
    }

    @Override
    public void logCertificateSend(String certificateId, String recipientId, String certificateType) {
        logEvent(MonitoringEvent.CERTIFICATE_SEND, certificateId, certificateType, recipientId);
    }

    @Override
    public void logCertificateArchived(String certificateId) {
        logEvent(MonitoringEvent.CERTIFICATE_ARCHIVED, certificateId);
    }

    @Override
    public void logCertificateRestored(String certificateId) {
        logEvent(MonitoringEvent.CERTIFICATE_RESTORED, certificateId);
    }

    @Override
    public void logBrowserInfo(String browserName, String browserVersion, String osFamily, String osVersion, String width, String height) {
        logEvent(MonitoringEvent.BROWSER_INFO, browserName, browserVersion, osFamily, osVersion, width, height);
    }

    @Override
    public void logCertificatePrintedFully(String certificateId, String certificateType) {
        logEvent(MonitoringEvent.CERTIFICATE_PRINTED_FULLY, certificateId, certificateType);
    }

    @Override
    public void logCertificatePrintedEmployerCopy(String certificateId, String certificateType) {
        logEvent(MonitoringEvent.CERTIFICATE_PRINTED_EMPLOYER_COPY, certificateId, certificateType);
    }

    @Override
    public void logSamlStatusForFailedLogin(String issuer, String samlStatus) {
        logEvent(MonitoringEvent.SAML_STATUS_LOGIN_FAIL, issuer, samlStatus);
    }

    @Override
    public void logOpenedAbout(String pnr) {
        logEvent(MonitoringEvent.OPENED_ABOUT, getPersonnummerHash(pnr));
    }

    @Override
    public void logOpenedFAQ(String pnr) {
        logEvent(MonitoringEvent.OPENED_FAQ, getPersonnummerHash(pnr));
    }

    @Override
    public void logOpenedQuestion(String id, String title, String pnr) {
        logEvent(MonitoringEvent.OPENED_QUESTION, id, title.replace(" ", "_"), getPersonnummerHash(pnr));
    }

    private String getPersonnummerHash(String pnr) {
        Optional<Personnummer> personnummer = Personnummer.createPersonnummer(pnr);
        return personnummer.get().getPersonnummerHash();
    }

    private void logEvent(MonitoringEvent logEvent, Object... logMsgArgs) {

        StringBuilder logMsg = new StringBuilder();
        logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());

        LOG.info(LogMarkers.MONITORING, logMsg.toString(), logMsgArgs);
    }


    private enum MonitoringEvent {
        BROWSER_INFO("Name '{}' Version '{}' OSFamily '{}' OSVersion '{}' Width '{}' Height '{}'"),
        CITIZEN_LOGIN("Citizen '{}' logged in using login method '{}'"),
        CITIZEN_LOGOUT("Citizen '{}' logged out using login method '{}'"),
        CERTIFICATE_READ("Certificate '{}' of type '{}' was read"),
        CERTIFICATE_SEND("Certificate '{}' of type '{}' sent to '{}'"),
        CERTIFICATE_ARCHIVED("Certificate '{}' archived"),
        CERTIFICATE_RESTORED("Certificate '{}' restored"),
        CERTIFICATE_PRINTED_FULLY("Certificate '{}' of type '{}' was printed including all information"),
        CERTIFICATE_PRINTED_EMPLOYER_COPY("Certificate '{}' of type '{}' was printed as employer copy"),
        OPENED_ABOUT("Om Mina intyg was opened by user '{}'"),
        OPENED_FAQ("FAQ for Mina intyg was opened by user '{}'"),
        OPENED_QUESTION("Question '{}' with title '{}' was opened by user '{}'"),

        SAML_STATUS_LOGIN_FAIL("Login failed at IDP '{}' with status message '{}'");

        private final String msg;

        MonitoringEvent(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }
    }

}
