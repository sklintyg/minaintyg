/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.auth;

import java.util.List;
import org.opensaml.common.SAMLObject;
import org.opensaml.saml2.core.impl.IssuerImpl;
import org.opensaml.saml2.core.impl.StatusImpl;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLConstants;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import se.inera.intyg.minaintyg.web.service.MonitoringLogService;

public class SAMLStatusLogger extends SAMLDefaultLogger {

    private final Logger log = LoggerFactory.getLogger(SAMLStatusLogger.class);

    @Autowired
    private MonitoringLogService logService;

    @Override
    public void log(String operation, String result, SAMLMessageContext context) {
        log(operation, result, context, SecurityContextHolder.getContext().getAuthentication(), null);
    }

    @Override
    public void log(String operation, String result, SAMLMessageContext context, Exception e) {
        log(operation, result, context, SecurityContextHolder.getContext().getAuthentication(), e);
    }

    @Override
    public void log(String operation, String result, SAMLMessageContext context, Authentication a, Exception e) {

        if (!log.isWarnEnabled()) {
            return;
        }

        if (context.getInboundSAMLMessage() != null && SAMLConstants.FAILURE.equals(result)) {
            final var samlObj = context.getInboundSAMLMessage();

            final var tmp = samlObj.getOrderedChildren();

            String issuer = null;
            String statusCode = null;
            String statusMessage = null;

            for (XMLObject obj : tmp) {
                if (obj instanceof IssuerImpl) {
                    IssuerImpl issuerObj = (IssuerImpl) obj;
                    issuer = issuerObj.getValue();
                } else if (obj instanceof StatusImpl) {
                    StatusImpl statusObj = (StatusImpl) obj;
                    statusCode = statusObj.getStatusCode() != null ? statusObj.getStatusCode().getValue() : null;
                    statusMessage = statusObj.getStatusMessage() != null ? statusObj.getStatusMessage().getMessage() : null;
                }
            }

            final var exceptionMessage = e != null ? e.toString() : null;

            logService.logSamlStatusForFailedLogin(issuer, statusCode, statusMessage, exceptionMessage);
        }

        super.log(operation, result, context, a, e);
    }
}
