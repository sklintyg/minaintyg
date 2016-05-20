/**
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 * <p>
 * This file is part of rehabstod (https://github.com/sklintyg/rehabstod).
 * <p>
 * rehabstod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * rehabstod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.web.controller.moduleapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;

/**
 * @author Magnus Ekstrand on 2016-04-29.
 */
@RunWith(MockitoJUnitRunner.class)
public class LuaeFsModuleApiControllerTest extends ModuleApiControllerTest {

    private static final String JSON_PATH = "lakarutlatande/minimalt-luae_fs.json";
    private static final String TARGET = "FK";

    @BeforeClass
    public static void loadCertificateData() throws IOException {
        // Read JSON file
        certificateData = FileUtils.readFileToString(new ClassPathResource(JSON_PATH).getFile());

        // Map JSON to an LuaefsUtlatande object
        Utlatande utlatande = new CustomObjectMapper().readValue(certificateData, LuaefsUtlatande.class);

        List<Status> status = new ArrayList<>();
        status.add(new Status(CertificateState.SENT, TARGET, LocalDateTime.now()));
        CertificateMetaData meta = new CertificateMetaData();
        meta.setStatus(status);

        utlatandeHolder = new CertificateResponse(certificateData, utlatande, meta, false);
        internalModelHolder = new InternalModelHolder(certificateData);
    }

    @Before
    public void init() throws IOException {
        setPersonnummer("19121212-1212");
        setCertificateId("123456");
        setCertificateType("luae_fs");
    }

}
