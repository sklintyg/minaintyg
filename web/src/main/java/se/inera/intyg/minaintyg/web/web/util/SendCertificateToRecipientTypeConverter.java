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

package se.inera.intyg.minaintyg.web.web.util;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.SendCertificateToRecipientType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.SendCertificateToRecipientType.SkickatAv;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;

public final class SendCertificateToRecipientTypeConverter {

    private static final String PERSON_ID_ROOT = "1.2.752.129.2.1.3.1";
    private static final String MOTTAGARE_CODE_SYSTEM = "769bb12b-bd9f-4203-a5cd-fd14f2eb3b80";

    private SendCertificateToRecipientTypeConverter() {
    }

    public static SendCertificateToRecipientType convert(String intygsId, String personnummer,
            String skickatAvPersonId, String recipient) {
        SendCertificateToRecipientType request = new SendCertificateToRecipientType();
        request.setSkickatTidpunkt(LocalDateTime.now());
        request.setIntygsId(buildIntygId(intygsId));
        request.setPatientPersonId(buildPersonId(personnummer));
        request.setMottagare(buildPart(recipient));
        request.setSkickatAv(buildSkickatAv(skickatAvPersonId));
        return request;
    }

    private static IntygId buildIntygId(String intygsId) {
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31"); // IT:s root since unit hsaId is not available
        intygId.setExtension(intygsId);
        return intygId;
    }

    private static PersonId buildPersonId(String personnummer) {
        PersonId personId = new PersonId();
        personId.setRoot(PERSON_ID_ROOT);
        personId.setExtension(personnummer);
        return personId;
    }

    private static Part buildPart(String recipient) {
        PartKod partKod = PartKod.fromValue(recipient);
        Part part = new Part();
        part.setCode(partKod.name());
        part.setCodeSystem(MOTTAGARE_CODE_SYSTEM);
        part.setDisplayName(partKod.getDisplayName());
        return part;
    }

    private static SkickatAv buildSkickatAv(String skickatAvPersonId) {
        SkickatAv skickatAv = new SkickatAv();
        skickatAv.setPersonId(buildPersonId(skickatAvPersonId));
        return skickatAv;
    }

}
