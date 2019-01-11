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
package se.inera.intyg.minaintyg.web.integrationtest;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.luae_na.support.LuaenaEntryPoint;
import se.inera.intyg.common.luse.support.LuseEntryPoint;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ModuleApiControllerIT extends IntegrationTestBase {

    private static final String CITIZEN_CIVIC_REGISTRATION_NUMBER = "19010101-0101";

    @Before
    public void setup() {
        cleanup();
    }

    @After
    public void cleanup() {
        IntegrationTestUtility.revokeConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        IntegrationTestUtility.deleteCertificatesForCitizen(CITIZEN_CIVIC_REGISTRATION_NUMBER);
    }

    @Test
    public void testGetCertificateFk7263() {
        testGetCertificate(Fk7263EntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateTsBas() {
        testGetCertificate(TsBasEntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateTsDiabetes() {
        testGetCertificate(TsDiabetesEntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateLuse() {
        testGetCertificate(LuseEntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateLisjp() {
        testGetCertificate(LisjpEntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateLuaena() {
        testGetCertificate(LuaenaEntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateLuaefs() {
        testGetCertificate(LuaefsEntryPoint.MODULE_ID);
    }

    @Test
    public void testGetCertificateWithoutConsent() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String type = LuseEntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
                .redirects().follow(false).and().pathParams("type", type, "id", id)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{id}");
    }

    @Test
    public void testGetCertificateWithoutSession() {
        IntegrationTestUtility.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String type = LuseEntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        given().redirects().follow(false).and().pathParams("type", type, "id", id)
                .expect().statusCode(HttpServletResponse.SC_FORBIDDEN)
                .when().get("moduleapi/certificate/{type}/{id}");
    }

    @Test
    public void testGetCertificatePdf() {
        IntegrationTestUtility.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String type = TsBasEntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        Response response = given().cookie("ROUTEID", IntegrationTestUtility.routeId)
                .pathParams("type", type, "id", id)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{id}/pdf");

        assertNotNull(response.getBody().asByteArray());
        String contentHeader = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentHeader);
        assertTrue(contentHeader.contains("attachment; filename="));
    }

    @Test
    public void testGetCertificatePdfEmployerCopy() {
        IntegrationTestUtility.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String type = Fk7263EntryPoint.MODULE_ID;
        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        Response response = given()
                .contentType(ContentType.URLENC).and().cookie("ROUTEID", IntegrationTestUtility.routeId)
                .pathParams("type", type, "id", id)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().post("moduleapi/certificate/{type}/{id}/pdf/arbetsgivarutskrift");

        assertNotNull(response.getBody().asByteArray());
        String contentHeader = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentHeader);
        assertTrue(contentHeader.contains("attachment; filename="));
    }

    @Test
    public void testGetRevokedCertificate() {
        IntegrationTestUtility.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, LuaenaEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, true, false);

        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
                .pathParams("type", LuaenaEntryPoint.MODULE_ID, "id", id)
                .expect().statusCode(HttpServletResponse.SC_GONE)
                .when().get("moduleapi/certificate/{type}/{id}");

    }

    private void testGetCertificate(String type) {
        IntegrationTestUtility.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
                .pathParams("type", type, "id", id)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{id}")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/generic-get-certificate-response-schema.json"));

    }

    @Test
    public void testModuleApiResponseNotCacheable() {
        IntegrationTestUtility.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        final String type = Fk7263EntryPoint.MODULE_ID;

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, type, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        // Note! The extra JSESSIONID cookie is to workaround a (testing) issue where the server responds
        // with a Set-Cookie: JSESSIONID=..... which also means that the server will NOT include and Cache-Control header
        // in that particular response. This is not an issue when running in a browser where both JSESSIONID and SESSION
        // are included in each request. We want to get rid of the JSESSIONID since we're relying on SESSION but
        // that's easier said than done with Spring Security + Spring Session...
        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
                .cookie("JSESSIONID", IntegrationTestUtility.jsessionId)
                .pathParams("type", type, "id", id)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("moduleapi/certificate/{type}/{id}")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/generic-get-certificate-response-schema.json"))
                .header("x-frame-options", equalTo("DENY"))
                .header("Cache-Control", equalTo("no-cache, no-store, max-age=0, must-revalidate"));

    }
}
