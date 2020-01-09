/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientType;

public class SendCertificateToRecipientTypeConverterTest {

    private final String intygsId = "intygsId";

    private final Personnummer pnrPatient = Personnummer.createPersonnummer("19121212-1212").get();
    private final Personnummer pnrSkickatAv = Personnummer.createPersonnummer("19101010-1010").get();


    @Test
    public void testConvert() {
        final String recipient = "TRANSP";

        SendCertificateToRecipientType result =
            SendCertificateToRecipientTypeConverter.convert(intygsId, pnrPatient, pnrSkickatAv, recipient);

        assertNotNull(result.getSkickatTidpunkt());
        assertNotNull(result.getIntygsId().getRoot());
        assertEquals(intygsId, result.getIntygsId().getExtension());
        assertNotNull(result.getPatientPersonId().getRoot());
        assertEquals(pnrPatient.getPersonnummer(), result.getPatientPersonId().getExtension());
        assertNotNull(result.getSkickatAv().getPersonId().getRoot());
        assertEquals(pnrSkickatAv.getPersonnummer(), result.getSkickatAv().getPersonId().getExtension());
        assertEquals(recipient, result.getMottagare().getCode());
        assertNotNull(result.getMottagare().getCodeSystem());
    }

    @Test
    public void testConvertRemovesDash() {
        final String recipient = "TS";

        SendCertificateToRecipientType result =
            SendCertificateToRecipientTypeConverter.convert(intygsId, pnrPatient, pnrSkickatAv, recipient);

        assertEquals("191212121212", result.getPatientPersonId().getExtension());
        assertEquals("191010101010", result.getSkickatAv().getPersonId().getExtension());
    }
}
