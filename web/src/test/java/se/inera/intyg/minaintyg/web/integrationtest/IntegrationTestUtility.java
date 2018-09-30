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

import se.inera.intyg.clinicalprocess.healthcond.certificate.registerapprovedreceivers.v1.ReceiverApprovalStatus;
import se.inera.intyg.clinicalprocess.healthcond.certificate.registerapprovedreceivers.v1.RegisterApprovedReceiversType;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.receiver.types.v1.ApprovalStatusType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;

public final class IntegrationTestUtility {

    public static final String FKASSA = "FKASSA";
    public static final String FBA = "FBA";

    // public static String certificateBaseUrl;
    public static String routeId;
    public static String jsessionId;

    private IntegrationTestUtility() {
    }

    public static String login(String personId) {
        Map<String, String> cookies = given().redirects().follow(false).and()
                .expect().statusCode(HttpServletResponse.SC_FOUND)
                .when().get("web/sso?guid=" + personId).getCookies();

        routeId = cookies.containsKey("ROUTEID") ? cookies.get("ROUTEID") : "nah";
        jsessionId = cookies.containsKey("JSESSIONID") ? cookies.get("JSESSIONID") : null;
        return cookies.get("SESSION");
    }

    public static void logout() {
        given().redirects().follow(false).and()
                .expect().statusCode(lessThan(404)) //statusCode(HttpServletResponse.SC_FOUND)
                .when().get("web/logga-ut");
    }

    public static void addConsent(String personId){
        given().get("testability/anvandare/consent/give/" + personId).then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void revokeConsent(String personId){
        given().get("testability/anvandare/consent/revoke/" + personId)
                .then().statusCode(200);
    }

    public static void deleteIntyg(String id) {
        given().delete("testability/resources/certificate/" + id)
                .then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void deleteCertificatesForCitizen(String personId) {
        given().delete("testability/resources/certificate/citizen/" + personId)
                .then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void givenIntyg(String intygsId, String intygTyp, String intygTypVersion, String personId, boolean revoked, boolean archived) {
        given()
                .body(certificate(intygsId, intygTyp, intygTypVersion, personId, revoked, archived))
                .post("testability/resources/certificate/")
                .then().statusCode(HttpServletResponse.SC_OK);
    }

    public static void givenReceivers(String intygsId){
        given()
                .body(createApprovedReceivers(intygsId, FKASSA, FBA))
                .post("testability/resources/certificate/" + intygsId + "/approvedreceivers/")
                .then().statusCode(HttpServletResponse.SC_OK);
    }

    private static CertificateHolder certificate(String intygsId, String intygTyp, String intygTypVersion, String personId, boolean revoked, boolean archived) {
        CertificateHolder certificate = new CertificateHolder();
        certificate.setId(intygsId);
        certificate.setType(intygTyp);
        certificate.setTypeVersion(intygTypVersion);
        certificate.setSignedDate(LocalDateTime.now());
        certificate.setCareGiverId("CareGiverId");
        certificate.setCareUnitId("CareUnitId");
        certificate.setCareUnitName("CareUnitName");
        certificate.setSigningDoctorName("Singing Doctor");
        certificate.setCivicRegistrationNumber(Personnummer.createPersonnummer(personId).get());
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

    private static RegisterApprovedReceiversType createApprovedReceivers(String intygsId, String... receivers) {
        final String intygsTyp = LisjpEntryPoint.MODULE_ID;
        final String displayName = LisjpEntryPoint.MODULE_NAME;

        IntygId intygId = new IntygId();
        intygId.setExtension(intygsId);
        intygId.setRoot("some root");

        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(intygsTyp);
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(displayName);

        RegisterApprovedReceiversType type = new RegisterApprovedReceiversType();
        type.setIntygId(intygId);
        type.setTypAvIntyg(typAvIntyg);

        for (String receiver : receivers) {
            type.getApprovedReceivers().add(createReceiverApprovalStatus(receiver, ApprovalStatusType.YES));
        }

        return type;
    }

    private static ReceiverApprovalStatus createReceiverApprovalStatus(String receiverId, ApprovalStatusType statusType) {
        ReceiverApprovalStatus approvalStatus = new ReceiverApprovalStatus();
        approvalStatus.setReceiverId(receiverId);
        approvalStatus.setApprovalStatus(statusType);
        return approvalStatus;
    }

}
