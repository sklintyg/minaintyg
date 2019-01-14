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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.luae_na.support.LuaenaEntryPoint;
import se.inera.intyg.common.luse.support.LuseEntryPoint;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static se.inera.intyg.minaintyg.web.integrationtest.IntegrationTestUtility.spec;

public class ApiControllerIT extends IntegrationTestBase {

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
    public void testListCertificates() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuseEntryPoint.MODULE_ID, LUSE_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), Fk7263EntryPoint.MODULE_ID, FK7263_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LisjpEntryPoint.MODULE_ID, LISJP_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuaenaEntryPoint.MODULE_ID, LUAE_NA_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuaefsEntryPoint.MODULE_ID, LUAE_FS_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), TsDiabetesEntryPoint.MODULE_ID, TS_DIABETES_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER,
                false, false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), TsBasEntryPoint.MODULE_ID, TS_BAS_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);

        spec()
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-certificates-response-schema.json"))
                .body("", hasSize(greaterThanOrEqualTo(7)));
    }

    @Test
    public void testListCertificatesFiltersRevokedCertificates() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuseEntryPoint.MODULE_ID, LUSE_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, true,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), Fk7263EntryPoint.MODULE_ID, FK7263_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, true,
                false);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LisjpEntryPoint.MODULE_ID, LISJP_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);

        spec()
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-certificates-response-schema.json"))
                .body("", hasSize(1));
    }

    @Test
    public void testListCertificatesWithoutSession() {
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuseEntryPoint.MODULE_ID, LUSE_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                false);

        given().redirects().follow(false).expect().statusCode(HttpServletResponse.SC_FORBIDDEN)
                .when().get("api/certificates");
    }

    @Test
    public void testListArchivedCertificates() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuseEntryPoint.MODULE_ID, LUSE_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                true);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), Fk7263EntryPoint.MODULE_ID, FK7263_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                true);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LisjpEntryPoint.MODULE_ID, LISJP_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                true);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuaenaEntryPoint.MODULE_ID, LUAE_NA_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                true);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), LuaefsEntryPoint.MODULE_ID, LUAE_FS_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                true);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), TsDiabetesEntryPoint.MODULE_ID, TS_DIABETES_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER,
                false, true);
        IntegrationTestUtility.givenIntyg(UUID.randomUUID().toString(), TsBasEntryPoint.MODULE_ID, TS_BAS_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,
                true);

        spec()
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/archived")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-certificates-response-schema.json"))
                .body("", hasSize(greaterThanOrEqualTo(7)));
    }

    @Test
    public void testListRecipientsForCertificate() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String intygsId = UUID.randomUUID().toString();

        IntegrationTestUtility.givenIntyg(intygsId, LisjpEntryPoint.MODULE_ID, LISJP_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false,false);
        IntegrationTestUtility.givenReceivers(intygsId);

        spec()
                .pathParams("id", intygsId)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/{id}/recipients")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/list-recipients-response-schema.json"));
    }

    @Test
    public void testArchive() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String intygId = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(intygId, LuseEntryPoint.MODULE_ID, LUSE_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        spec()
                .pathParams("id", intygId)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().put("api/certificates/{id}/archive")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/certificate-meta-response-schema.json"));
    }

    @Test
    public void testRestore() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String intygId = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(intygId, LuaenaEntryPoint.MODULE_ID, LUAE_NA_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, true);

        spec()
                .pathParams("id", intygId)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().put("api/certificates/{id}/restore")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/certificate-meta-response-schema.json"));
    }

    @Test
    public void testOnBeforeUnload() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        String res = spec()
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/onbeforeunload").getBody().asString();

        assertEquals("ok", res);
    }

    @Test
    public void testGetUser() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        spec()
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/user")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-user-response-schema.json"));
    }

    @Test
    public void testGetQuestions() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        spec()
                .pathParams("type", LuseEntryPoint.MODULE_ID, "version", "1.0")
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().get("api/certificates/questions/{type}/{version}")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-questions-response-schema.json"));
    }

    @Test
    public void testSend() {
        createAuthSession(CITIZEN_CIVIC_REGISTRATION_NUMBER);

        final String id = UUID.randomUUID().toString();
        IntegrationTestUtility.givenIntyg(id, LuaenaEntryPoint.MODULE_ID, LUAE_NA_VERSION, CITIZEN_CIVIC_REGISTRATION_NUMBER, false, false);

        List<String> recipientList = Arrays.asList("FKASSA", "FAKE");
        spec()
                .pathParams("id", id).and().body(recipientList)
                .expect().statusCode(HttpServletResponse.SC_OK)
                .when().put("api/certificates/{id}/send")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/send-response-schema.json"));
    }

}
