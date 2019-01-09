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
package se.inera.intyg.minaintyg.web.util;

import org.junit.Before;
import org.junit.Test;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class CertificateMetaConverterTest {

    private static final String AVAILABLE = "available";
    private static final String CERTIFIED_ID = "certifiedId";
    private static final String TYPE = "type";
    private static final String TYPE_VERSION = "type";
    private static final String FACILITY_NAME = "facilityName";
    private static final String ISSUER_NAME = "issuerName";

    private UtlatandeMetaBuilder builder;

    @Before
    public void setupCertificateMetaTypeBuilder() {
        builder = new UtlatandeMetaBuilder();
        builder.id(CERTIFIED_ID)
                .type(TYPE)
                .typeVersion(TYPE_VERSION)
                .issuerName(ISSUER_NAME)
                .facilityName(FACILITY_NAME)
                .signDate(LocalDateTime.now())
                .available(AVAILABLE)
                .additionalInfo("2013-01-01 till 2014-01-01");
    }

    @Test
    public void testComplementaryInfo() {
        CertificateMeta meta = CertificateMetaConverter.toCertificateMetaFromUtlatandeMeta(builder.build());
        UtlatandeMetaData utlatandeMeta = builder.build();

        assertEquals("2013-01-01 till 2014-01-01", utlatandeMeta.getComplemantaryInfo());
        assertEquals("2013-01-01 till 2014-01-01", meta.getComplementaryInfo());
    }
}
