/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.web.web.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationRequest;
import org.callistasoftware.netcare.mvk.authentication.service.api.impl.AuthenticationResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * AOP Advice that optionally overrides the WS call that {@link org.callistasoftware.netcare.mvk.authentication.service.impl.MvkAuthenticationServiceImpl.authenticate} executes against MVK. The result
 * is overridden if you try to use a civic registration number as token.
 *
 * @author marced
 *
 */
@Aspect
public class MvkValidationServiceAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(MvkValidationServiceAdvice.class);

    @Value(value = "fakeMatcherRegExp")
    private String fakeMatcherRegExp;

    public void setFakeMatcherRegExp(String fakeMatcherRegExp) {
        this.fakeMatcherRegExp = fakeMatcherRegExp;
    }

    // CHECKSTYLE:OFF IllegalThrowsCheck
    @Around("execution(public org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult org.callistasoftware.netcare.mvk.authentication.service.impl.MvkAuthenticationServiceImpl.authenticate(..))")
    public Object overrideMVKAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthenticationRequest req = (AuthenticationRequest) joinPoint.getArgs()[0];
        // Get the token from the request
        String guid = req.getAuthenticationToken();
        if (guid != null && guid.matches(fakeMatcherRegExp)) {
            LOG.debug("'Fake' mvk token parameter detected - Mocking validation against MVK as {}...", guid);
            return AuthenticationResultImpl.newPatient(req.getAuthenticationToken());
        } else {
            LOG.debug("'Real' mvk token parameter detected - validating against MVK with token {}...", guid);
            return joinPoint.proceed();
        }
    }
    // CHECKSTYLE:ON IllegalThrowsCheck
}
