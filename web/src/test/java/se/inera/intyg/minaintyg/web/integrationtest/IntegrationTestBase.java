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
package se.inera.intyg.minaintyg.web.integrationtest;

import org.junit.After;
import org.junit.Before;

import com.google.common.base.Strings;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

/**
 * Base class for REST / SOAP in-container tests.
 */
public abstract class IntegrationTestBase {

    protected static final String LUSE_VERSION = "1.0";
    protected static final String FK7263_VERSION = "1.0";
    protected static final String TS_BAS_VERSION = "6.8";
    protected static final String TS_DIABETES_VERSION = "2.7";
    protected static final String LISJP_VERSION = "1.0";
    protected static final String LUAE_NA_VERSION = "1.0";
    protected static final String LUAE_FS_VERSION = "1.0";
    protected static final String AG7804_VERSION = "1.0";

    /**
     * Common setup for all tests
     */
    @Before
    public void setupBase() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = System.getProperty("integration.tests.baseUrl", "http://localhost:8088");
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.config = RestAssured.config().sessionConfig(RestAssured.config().getSessionConfig().sessionIdName("SESSION"));
        // IntegrationTestUtility.certificateBaseUrl = System.getProperty("integration.tests.certificate.baseUrl") + "inera-certificate/";
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
