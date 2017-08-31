/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.integrationtest;

import org.junit.After;
import org.junit.Before;

import com.google.common.base.Strings;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;

/**
 * Base class for REST / SOAP in-container tests.
 */
public abstract class IntegrationTestBase {

    /**
     * Common setup for all tests
     */
    @Before
    public void setupBase() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = System.getProperty("integration.tests.baseUrl");
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        IntegrationTestUtility.certificateBaseUrl = System.getProperty("integration.tests.certificate.baseUrl");
    }

    @After
    public void cleanupBase() {
        destroySession();
        RestAssured.reset();
    }

    protected static void createAuthSession(String personId) {
        RestAssured.sessionId = IntegrationTestUtility.login(personId);
    }

    protected static void createAuthSession() {
        createAuthSession("19121212-1212");
    }

    protected static void destroySession() {
        if (!Strings.isNullOrEmpty(RestAssured.sessionId)) {
            IntegrationTestUtility.logout();
        }
    }
}
