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
package se.inera.intyg.minaintyg.web.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.SendCertificateToRecipientType;

public class SendCertificateToRecipientTypeConverterTest {

    @Test
    public void testConvert() {
        final String intygsId = "intygsId";
        final String personnummer = "personnummer";
        final String skickatAvPersonId = "skickatavpid";
        final String recipient = "TS";

        SendCertificateToRecipientType result = SendCertificateToRecipientTypeConverter.convert(intygsId, new Personnummer(personnummer),
                new Personnummer(skickatAvPersonId), recipient);

        assertNotNull(result.getSkickatTidpunkt());
        assertNotNull(result.getIntygsId().getRoot());
        assertEquals(intygsId, result.getIntygsId().getExtension());
        assertNotNull(result.getPatientPersonId().getRoot());
        assertEquals(personnummer, result.getPatientPersonId().getExtension());
        assertNotNull(result.getSkickatAv().getPersonId().getRoot());
        assertEquals(skickatAvPersonId, result.getSkickatAv().getPersonId().getExtension());
        assertEquals("TRANSP", result.getMottagare().getCode());
        assertNotNull(result.getMottagare().getCodeSystem());
    }

    @Test
    public void testConvertRemovesDash() {
        final String intygsId = "intygsId";
        final String personnummer = "19121212-1212";
        final String skickatAvPersonId = "19101010-1010";
        final String recipient = "TS";

        SendCertificateToRecipientType result = SendCertificateToRecipientTypeConverter.convert(intygsId, new Personnummer(personnummer),
                new Personnummer(skickatAvPersonId), recipient);

        assertEquals("191212121212", result.getPatientPersonId().getExtension());
        assertEquals("191010101010", result.getSkickatAv().getPersonId().getExtension());
    }
}
