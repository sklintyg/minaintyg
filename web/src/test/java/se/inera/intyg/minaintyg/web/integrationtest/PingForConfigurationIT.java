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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.jayway.restassured.http.ContentType;

public class PingForConfigurationIT extends BaseIntegrationTest {

    private static final String BASE = "Envelope.Body.PingForConfigurationResponse.";
    private static final String PING_FOR_CONFIGURATION_V1_0 = "monitoring/ping-for-configuration/v1.0";

    private String request;

    @Before
    public void setup() throws IOException {
        request = FileUtils.readFileToString(new ClassPathResource("integrationtest/ping-for-configuration-request.xml").getFile());
    }

    @Test
    public void testPingForConfiguration() throws Exception {

        given().contentType(ContentType.XML)
                .body(request).when()
                .post(PING_FOR_CONFIGURATION_V1_0)
                .then().statusCode(200)
                .rootPath(BASE)
                .body("version", not(isEmptyString()))
                .body("pingDateTime", not(isEmptyString()))
                .body("configuration.size()", is(greaterThan(0)));

    }

}
