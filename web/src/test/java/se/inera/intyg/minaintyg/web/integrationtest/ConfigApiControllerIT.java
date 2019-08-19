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

/**
 * Created by marced on 2017-05-16.
 */
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

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

import javax.servlet.http.HttpServletResponse;
import org.junit.Test;

public class ConfigApiControllerIT extends IntegrationTestBase {

    @Test
    public void testGetConfig() {

        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
            .expect().statusCode(HttpServletResponse.SC_OK)
            .when().get("appconfig/api/app")
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-appconfig-response-schema.json"))
            .body("knownRecipients", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetModulesMap() {

        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
            .expect().statusCode(HttpServletResponse.SC_OK)
            .when().get("appconfig/api/map")
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-module-map-response-schema.json"))
            .body("", hasSize(greaterThanOrEqualTo(7)));
    }

    @Test
    public void testApiResponseNotCacheable() {

        given().cookie("ROUTEID", IntegrationTestUtility.routeId)
            .expect().statusCode(HttpServletResponse.SC_OK)
            .when().get("appconfig/api/map")
            .then()
            .header("Cache-Control", containsString("private"))
            .header("Cache-Control", containsString("no-cache"))
            .header("Cache-Control", containsString("no-store"))
            .header("Cache-Control", containsString("no-transform"))
            .header("Cache-Control", containsString("must-revalidate"))
            .header("Cache-Control", containsString("max-age=0"));
    }

}
