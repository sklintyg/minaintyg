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
package se.inera.intyg.minaintyg.web.integrationtest;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.junit.*;

import se.inera.intyg.intygstyper.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.intygstyper.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.intygstyper.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.intygstyper.luae_na.support.LuaenaEntryPoint;
import se.inera.intyg.intygstyper.luse.support.LuseEntryPoint;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.intygstyper.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.minaintyg.web.integrationtest.util.IntegrationTestUtil;

public class ApiControllerIT extends BaseIntegrationTest {

    private static final String CITIZEN_CIVIC_REGISTRATION_NUMBER = "19010101-0101";

    @Before
    public void setup() {
        cleanup();
        IntegrationTestUtil.addConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);
    }

    @After
    public void cleanup() {
        IntegrationTestUtil.revokeConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        IntegrationTestUtil.deleteCertificatesForCitizen(CITIZEN_CIVIC_REGISTRATION_NUMBER);
    }

    @Test
    public void testListCertificates() {
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LuseEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), Fk7263EntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LisjpEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LuaenaEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LuaefsEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), TsDiabetesEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), TsBasEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        given().expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-certificates-response-schema.json"))
                .body("", hasSize(greaterThanOrEqualTo(7)));
    }

    @Test
    public void testListArchivedCertificates() {
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LuseEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), Fk7263EntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LisjpEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LuaenaEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), LuaefsEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), TsDiabetesEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);
        IntegrationTestUtil.givenIntyg(UUID.randomUUID().toString(), TsBasEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);

        given().expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/archived")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-certificates-response-schema.json"))
                .body("", hasSize(greaterThanOrEqualTo(7)));
    }

    @Test
    public void testListRecipientsFK() {
        given().pathParams("type", Fk7263EntryPoint.MODULE_ID)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/{type}/recipients")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-recipients-response-schema.json"));
    }

    @Test
    public void testListRecipientsTS() {
        given().pathParams("type", TsBasEntryPoint.MODULE_ID)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/{type}/recipients")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-recipients-response-schema.json"));
    }

    @Test
    public void testArchive() {
        final String intygId = UUID.randomUUID().toString();
        IntegrationTestUtil.givenIntyg(intygId, LuseEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        given().pathParams("id", intygId)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().put("api/certificates/{id}/archive")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/certificate-meta-response-schema.json"));
    }

    @Test
    public void testRestore() {
        final String intygId = UUID.randomUUID().toString();
        IntegrationTestUtil.givenIntyg(intygId, LuaenaEntryPoint.MODULE_ID, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);

        given().pathParams("id", intygId)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().put("api/certificates/{id}/restore")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/certificate-meta-response-schema.json"));
    }

    @Test
    public void testGiveConsent() {
        IntegrationTestUtil.revokeConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        given().expect().statusCode(HttpServletResponse.SC_OK)
                .when().post("api/certificates/consent/give")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/consent-response-schema.json"));
    }

    @Test
    public void testRevokeConsent() {
        given().expect().statusCode(HttpServletResponse.SC_OK)
                .when().post("api/certificates/consent/revoke")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/consent-response-schema.json"));
    }

    @Test
    public void testGetModulesMap() {
        IntegrationTestUtil.revokeConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER); // no consent required
        given().expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/map")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-module-map-response-schema.json"))
                .body("", hasSize(greaterThanOrEqualTo(7)));
    }

    @Test
    public void testOnBeforeUnload() {
        IntegrationTestUtil.revokeConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER); // no consent required
        String res = given().expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/onbeforeunload").getBody().asString();

        assertEquals("ok", res);
    }

    @Test
    public void testGetQuestions() {
        IntegrationTestUtil.revokeConsent(CITIZEN_CIVIC_REGISTRATION_NUMBER); // no consent required
        given().pathParams("type", LuseEntryPoint.MODULE_ID, "version", "1.0")
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/questions/{type}/{version}")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-questions-response-schema.json"));
    }

}
