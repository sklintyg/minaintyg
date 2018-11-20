/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.luae_na.support.LuaenaEntryPoint;
import se.inera.intyg.common.luse.support.LuseEntryPoint;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.minaintyg.web.integrationtest.IntegrationTestUtility.spec;

public class ModuleApiControllerIT extends IntegrationTestBase {

    private static final String CITIZEN_CIVIC_REGISTRATION_NUMBER = "19010101-0101";

    @Before
    public void setup() {
        cleanup();
    }

    @After
    public void cleanup() {
        IntegrationTestUtility.deleteCertificatesForCitizen(CITIZEN_CIVIC_REGISTRATION_NUMBER);
    }

    @Test
    public void testGetCertificateFk7263() {
        testGetCertificate(Fk7263EntryPoint.MODULE_ID, FK7263_VERSION);
    }

    @Test
    public void testGetCertificateTsBas() {
        testGetCertificate(TsBasEntryPoint.MODULE_ID, TS_BAS_VERSION);
    }

    @Test
    public void testGetCertificateTsDiabetes() {
        testGetCertificate(TsDiabetesEntryPoint.MODULE_ID, TS_DIABETES_VERSION);
    }

    @Test
    public void testGetCertificateLuse() {
        testGetCertificate(LuseEntryPoint.MODULE_ID, LUSE_VERSION);
    }

    @Test
    public void testGetCertificateLisjp() {
        testGetCertificate(LisjpEntryPoint.MODULE_ID, LISJP_VERSION);
    }

    @Test
    public void testGetCertificateLuaena() {
        testGetCertificate(LuaenaEntryPoint.MODULE_ID, LUAE_NA_VERSION);
    }

    @Test
    public void testGetCertificateLuaefs() {
        testGetCertificate(LuaefsEntryPoint.MODULE_ID, LUAE_FS_VERSION);
    }

    @Test
    public void testGetCertificateWithoutSession() {
        final String type = LuseEntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, LUSE_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        given().redirects().follow(false).and().pathParams("type", type, "id", id, "intygTypeVersion", LUSE_VERSION)
                .expect().statusCode(HttpServletResponse.SC_FORBIDDEN)
                .when().get("moduleapi/certificate/{type}/{intygTypeVersion}/{id}");
    }

    @Test
    public void testGetCertificatePdf() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String type = TsBasEntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, TS_BAS_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        Response response = spec()
                .pathParams("type", type, "id", id, "intygTypeVersion", TS_BAS_VERSION)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{intygTypeVersion}/{id}/pdf");

        assertNotNull(response.getBody().asByteArray());
        String contentHeader = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentHeader);
        assertTrue(contentHeader.contains("attachment; filename="));
    }

    @Test
    public void testGetCertificatePdfEmployerCopy() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String type = Fk7263EntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, FK7263_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        Response response = spec()
                .contentType(ContentType.URLENC)
                .pathParams("type", type, "id", id, "intygTypeVersion", FK7263_VERSION)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().post("moduleapi/certificate/{type}/{intygTypeVersion}/{id}/pdf/arbetsgivarutskrift");

        assertNotNull(response.getBody().asByteArray());
        String contentHeader = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentHeader);
        assertTrue(contentHeader.contains("attachment; filename="));
    }

    @Test
    public void testGetRevokedCertificate() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, LuaenaEntryPoint.MODULE_ID, LUAE_NA_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, true, false);

        spec()
                .pathParams("type", LuaenaEntryPoint.MODULE_ID, "id", id, "intygTypeVersion", LUAE_NA_VERSION)
                .expect().statusCode(HttpServletResponse.SC_GONE)
                .when().get("moduleapi/certificate/{type}/{intygTypeVersion}/{id}");

    }

    private void testGetCertificate(String type, String intygTypeVersion) {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, intygTypeVersion, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        spec()
                .pathParams("type", type, "id", id, "intygTypeVersion", intygTypeVersion)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{intygTypeVersion}/{id}")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/generic-get-certificate-response-schema.json"));

    }

    @Test
    public void testModuleApiResponseNotCacheable() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        final String type = Fk7263EntryPoint.MODULE_ID;

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, FK7263_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        // Note! The extra JSESSIONID cookie is to workaround a (testing) issue where the server responds
        // with a Set-Cookie: JSESSIONID=..... which also means that the server will NOT include and Cache-Control header
        // in that particular response. This is not an issue when running in a browser where both JSESSIONID and SESSION
        // are included in each request. We want to get rid of the JSESSIONID since we're relying on SESSION but
        // that's easier said than done with Spring Security + Spring Session...
        spec()
                .cookie("JSESSIONID", IntegrationTestUtility.jsessionId)
                .pathParams("type", type, "id", id, "intygTypeVersion", FK7263_VERSION)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{intygTypeVersion}/{id}")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/generic-get-certificate-response-schema.json"))
                .header("x-frame-options", equalTo("DENY"))
                .header("Cache-Control", equalTo("no-cache, no-store, max-age=0, must-revalidate"));

    }
}
