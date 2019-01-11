/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.minaintyg.web.service.CitizenService;

/**
 * Created by orjan on 14-08-19(34).
 */
public class VerifyConsentJAXRSInvoker extends JAXRSInvoker {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyConsentJAXRSInvoker.class);


    @Autowired
    private CitizenService citizenService;
    private AtomicLong consentCounter = new AtomicLong();

    @Override
    public Object invoke(Exchange exchange, Object requestParams, Object resourceObject) {
        LOG.debug("invoke(...)");

        LOG.debug("Verifying citizen consent...");
        // Get Citizen instance from context
        Citizen citizen = citizenService.getCitizen();
        if (citizen == null) {
            LOG.error("State of consent not known -citizen was null");
            return new MessageContentsList(Response.status(Response.Status.FORBIDDEN).build());
        }

        LOG.debug("Login from {} consentIsKnown: {} ", citizen.getLoginMethod().toString(), citizen.consentIsKnown());

        // We have a consent, let the request continue processing
        consentCounter.incrementAndGet();

        return super.invoke(exchange, requestParams, resourceObject);
    }

    public Long getConsentCounter() {
        return consentCounter.longValue();
    }

}

// Servlet filters -> CXF interceptors.
//
// If you mix JAX-RS 2.0 filters & CXF interceptors, then
//
// Servlet filters -> CXF interceptors before UNMARSHAL phase ->
// PreMatch Container Request Filter -> CXF interceptors after UNMARSHAL
// phase -> per-method specific Container Request Filters
