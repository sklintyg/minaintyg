/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.*;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;

public class CertificateMetaConverterTest {

    private static final String AVAILABLE = "available";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String TYPE = "type";
    private static final String FACILITY_NAME = "facilityName";
    private static final String ISSUER_NAME = "issuerName";

    private static final LocalDateTime FIRST_TIMESTAMP = new LocalDateTime(2013, 1, 2, 20, 0);
    private static final LocalDateTime LATER_TIMESTAMP = new LocalDateTime(2013, 1, 3, 20, 0);
    private static final String TARGET = "FK";

    private static Status sentStatus;
    private static Status cancelledStatus;

    private UtlatandeMetaBuilder builder;

    @BeforeClass
    public static void setup() {
        sentStatus = new Status(CertificateState.SENT, TARGET, FIRST_TIMESTAMP);
        cancelledStatus = new Status(CertificateState.CANCELLED, TARGET, LATER_TIMESTAMP);
    }

    @Before
    public void setupCertificateMetaTypeBuilder() {
        builder = new UtlatandeMetaBuilder();
        builder.id(CERTIFIED_ID)
                .type(TYPE)
                .issuerName(ISSUER_NAME)
                .facilityName(FACILITY_NAME)
                .signDate(new LocalDateTime())
                .available(AVAILABLE)
                .additionalInfo("2013-01-01 till 2014-01-01");
    }

    @Test
    public void testIsCertificateCancelledWithNullList() {
        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        assertFalse(meta.getCancelled());
    }

    @Test
    public void testIsCertificateCancelledWithoutCancelStatus() {
        builder.addStatus(sentStatus);

        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        assertFalse(meta.getCancelled());
    }

    @Test
    public void testIsCertificateCancelledWithCancelStatus() {
        builder.addStatus(sentStatus);
        builder.addStatus(cancelledStatus);

        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        assertTrue(meta.getCancelled());
    }

    @Test
    public void testAdditionalInfo() {
        CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(builder.build());
        UtlatandeMetaData utlatandeMeta = builder.build();

        assertEquals("2013-01-01 till 2014-01-01", utlatandeMeta.getComplemantaryInfo());
        assertEquals("2013-01-01 till 2014-01-01", meta.getComplementaryInfo());
    }
}
