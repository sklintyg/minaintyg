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
package se.inera.intyg.minaintyg.web.integrationtest.util;

import static com.jayway.restassured.RestAssured.given;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

public final class IntegrationTestUtil {

    public static String certificateBaseUrl;

    private IntegrationTestUtil() {
    }

    public static String login(String personId) {
        return given().redirects().follow(false).and().expect().statusCode(HttpServletResponse.SC_FOUND)
                .when().get("web/sso?guid=" + personId).sessionId();
    }

    public static void logout() {
        given().redirects().follow(false).and().expect().statusCode(HttpServletResponse.SC_FOUND)
                .when().get("web/logga-ut");
    }

    public static void addConsent(String personId){
        given().get("testability/anvandare/consent/give/" + personId).then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void revokeConsent(String personId){
        given().get("testability/anvandare/consent/revoke/" + personId).then().statusCode(200);
    }

    public static void deleteIntyg(String id) {
        given().baseUri(certificateBaseUrl).delete("resources/certificate/" + id).then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void deleteCertificatesForCitizen(String personId) {
        given().baseUri(certificateBaseUrl).delete("resources/certificate/citizen/" + personId).then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void givenIntyg(String intygId, String intygTyp, String personId, boolean revoked, boolean archived) {
        given().baseUri(certificateBaseUrl).body(certificate(intygId, intygTyp, personId, revoked, archived))
            .post("resources/certificate/").then().statusCode(HttpServletResponse.SC_OK);
    }

    private static CertificateHolder certificate(String intygId, String intygTyp, String personId, boolean revoked, boolean archived) {
        CertificateHolder certificate = new CertificateHolder();
        certificate.setId(intygId);
        certificate.setType(intygTyp);
        certificate.setSignedDate(LocalDateTime.now());
        certificate.setCareGiverId("CareGiverId");
        certificate.setCareUnitId("CareUnitId");
        certificate.setCareUnitName("CareUnitName");
        certificate.setSigningDoctorName("Singing Doctor");
        certificate.setCivicRegistrationNumber(new Personnummer(personId));
        certificate.setCertificateStates(new ArrayList<>());
        certificate.getCertificateStates().add(new CertificateStateHolder("HV", CertificateState.RECEIVED, LocalDateTime.now()));
        if (revoked) {
            certificate.getCertificateStates().add(new CertificateStateHolder("HV", CertificateState.CANCELLED, LocalDateTime.now()));
        }
        if (archived) {
            certificate.getCertificateStates().add(new CertificateStateHolder("MI", CertificateState.DELETED, LocalDateTime.now()));
        }
        certificate.setRevoked(revoked);
        return certificate;
    }
}
