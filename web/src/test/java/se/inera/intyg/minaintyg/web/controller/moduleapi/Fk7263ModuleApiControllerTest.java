/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.moduleapi;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * @author Magnus Ekstrand on 2016-04-29.
 */
@RunWith(MockitoJUnitRunner.class)
public class Fk7263ModuleApiControllerTest extends ModuleApiControllerTest {

    private static final String JSON_PATH = "lakarutlatande/maximalt-fk7263.json";
    private static final String TARGET = "FK";
    private static final String CERTIFICATE_TYPE_VERSION = "1.0";

    @BeforeClass
    public static void setupCertificateData() throws IOException {
        // Read JSON file
        certificateData = Resources.toString(new ClassPathResource(JSON_PATH).getURL(), Charsets.UTF_8);

        // Map JSON to an Utlatande object
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(certificateData, Fk7263Utlatande.class);

        List<Status> status = new ArrayList<>();
        status.add(new Status(CertificateState.SENT, TARGET, LocalDateTime.now()));
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(status);

        List<Status> revokedStatus = new ArrayList<>();
        status.add(new Status(CertificateState.CANCELLED, TARGET, LocalDateTime.now()));
        CertificateMetaData revokedMeta = new CertificateMetaData();
        revokedMeta.setStatus(revokedStatus);

        utlatandeHolder = new CertificateResponse(certificateData, utlatande, meta, false);
        revokedUtlatandeHolder = new CertificateResponse(certificateData, utlatande, revokedMeta, true);
    }

    @Before
    public void init() throws IOException {
        setPersonnummer("19121212-1212");
        setCertificateId("123456");
        setCertificateType("fk7263");
        setCertificateTypeVersion(CERTIFICATE_TYPE_VERSION);
    }

}
