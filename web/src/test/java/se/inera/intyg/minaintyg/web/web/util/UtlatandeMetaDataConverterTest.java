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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.registry.ModuleNotFoundException;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.*;
import se.riv.clinicalprocess.healthcond.certificate.v3.*;

@RunWith(MockitoJUnitRunner.class)
public class UtlatandeMetaDataConverterTest {

    @Mock
    private ModuleApi moduleApi;

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @InjectMocks
    private UtlatandeMetaDataConverter converter;

    @Before
    public void setup() throws ModuleNotFoundException {
        when(moduleRegistry.getModuleApi(anyString())).thenReturn(moduleApi);
        when(moduleRegistry.getModuleIdFromExternalId(anyString())).thenAnswer(invocation -> ((String) invocation.getArguments()[0]).toLowerCase());
    }

    @Test
    public void convertTest() throws Exception {
        final String intygId = "intygsid";
        final String intygstyp = "LUSE";
        final String fullstandigtNamn = "fullständigt namn";
        final String enhetsnamn = "enhetsnamn";
        final String additionalInfo = "additionalInfo";
        final LocalDateTime signeringstidpunkt = LocalDateTime.now();
        final boolean arkiverade = false;
        when(moduleApi.getAdditionalInfo(any(Intyg.class))).thenReturn(additionalInfo);
        Intyg intyg = buildIntyg(intygId, intygstyp, fullstandigtNamn, enhetsnamn, signeringstidpunkt);
        UtlatandeMetaData result = converter.convert(intyg, arkiverade);
        assertNotNull(result);
        assertEquals(intygId, result.getId());
        assertEquals(intygstyp.toLowerCase(), result.getType());
        assertEquals(fullstandigtNamn, result.getIssuerName());
        assertEquals(enhetsnamn, result.getFacilityName());
        assertEquals(signeringstidpunkt, result.getSignDate());
        assertEquals("true", result.getAvailable());
        assertEquals(additionalInfo, result.getComplemantaryInfo());

        verify(moduleRegistry).getModuleApi(intygstyp.toLowerCase());
        verify(moduleApi).getAdditionalInfo(any(Intyg.class));
    }

    @Test
    public void convertMultipleSortsTest() throws Exception {
        final String intygId1 = "intygsid1";
        final String intygstyp1 = "FK7263";
        final LocalDateTime signeringstidpunkt1 = LocalDateTime.now();
        final String intygId2 = "intygsid2";
        final String intygstyp2 = "LUSE";
        final LocalDateTime signeringstidpunkt2 = signeringstidpunkt1.minusDays(4);
        final String intygId3 = "intygsid3";
        final String intygstyp3 = "LISJP";
        final LocalDateTime signeringstidpunkt3 = signeringstidpunkt1.plusDays(3);
        final boolean arkiverade = false;
        Intyg intyg1 = buildIntyg(intygId1, intygstyp1, "fullstandigtNamn", "enhetsnamn", signeringstidpunkt1);
        Intyg intyg2 = buildIntyg(intygId2, intygstyp2, "fullstandigtNamn", "enhetsnamn", signeringstidpunkt2);
        Intyg intyg3 = buildIntyg(intygId3, intygstyp3, "fullstandigtNamn", "enhetsnamn", signeringstidpunkt3);
        List<UtlatandeMetaData> result = converter.convert(Arrays.asList(intyg1, intyg2, intyg3), arkiverade);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(intygId3, result.get(0).getId());
        assertEquals(intygstyp3.toLowerCase(), result.get(0).getType());
        assertEquals(signeringstidpunkt3, result.get(0).getSignDate());
        assertEquals(intygId1, result.get(1).getId());
        assertEquals(intygstyp1.toLowerCase(), result.get(1).getType());
        assertEquals(signeringstidpunkt1, result.get(1).getSignDate());
        assertEquals(intygId2, result.get(2).getId());
        assertEquals(intygstyp2.toLowerCase(), result.get(2).getType());
        assertEquals(signeringstidpunkt2, result.get(2).getSignDate());

        verify(moduleRegistry).getModuleApi(intygstyp1.toLowerCase());
        verify(moduleRegistry).getModuleApi(intygstyp2.toLowerCase());
        verify(moduleRegistry).getModuleApi(intygstyp3.toLowerCase());
        verify(moduleApi, times(3)).getAdditionalInfo(any(Intyg.class));
    }

    @Test
    public void convertStatusSentTest() {
        final LocalDateTime statusTidpunkt = LocalDateTime.now().minusDays(4);
        final String recipient = "FKASSA";
        Intyg intyg = buildIntyg("intygId", "intygstyp", "fullstandigtNamn", "enhetsnamn", LocalDateTime.now());
        IntygsStatus intygsstatus = new IntygsStatus();
        intygsstatus.setStatus(new Statuskod());
        intygsstatus.getStatus().setCode("SENTTO");
        intygsstatus.setTidpunkt(statusTidpunkt);
        intygsstatus.setPart(new Part());
        intygsstatus.getPart().setCode(recipient);
        intyg.getStatus().add(intygsstatus);

        UtlatandeMetaData result = converter.convert(intyg, false);
        assertNotNull(result);
        assertNotNull(result.getStatuses());
        assertEquals(1, result.getStatuses().size());
        assertEquals(CertificateState.SENT, result.getStatuses().get(0).getType());
        assertEquals("FK", result.getStatuses().get(0).getTarget());
        assertEquals(statusTidpunkt, result.getStatuses().get(0).getTimestamp());
    }

    @Test
    public void convertStatusCancelledTest() {
        final LocalDateTime statusTidpunkt = LocalDateTime.now().minusDays(4);
        final String recipient = "TRANSP";
        Intyg intyg = buildIntyg("intygId", "intygstyp", "fullstandigtNamn", "enhetsnamn", LocalDateTime.now());
        IntygsStatus intygsstatus = new IntygsStatus();
        intygsstatus.setStatus(new Statuskod());
        intygsstatus.getStatus().setCode("CANCEL");
        intygsstatus.setTidpunkt(statusTidpunkt);
        intygsstatus.setPart(new Part());
        intygsstatus.getPart().setCode(recipient);
        intyg.getStatus().add(intygsstatus);

        UtlatandeMetaData result = converter.convert(intyg, false);
        assertNotNull(result);
        assertNotNull(result.getStatuses());
        assertEquals(1, result.getStatuses().size());
        assertEquals(CertificateState.CANCELLED, result.getStatuses().get(0).getType());
        assertEquals("TS", result.getStatuses().get(0).getTarget());
        assertEquals(statusTidpunkt, result.getStatuses().get(0).getTimestamp());
    }

    @Test
    public void convertOtherStatusTest() {
        Intyg intyg = buildIntyg("intygId", "intygstyp", "fullstandigtNamn", "enhetsnamn", LocalDateTime.now());
        IntygsStatus intygsstatus = new IntygsStatus();
        intygsstatus.setStatus(new Statuskod());
        intygsstatus.getStatus().setCode("DELETE");
        intygsstatus.setTidpunkt(LocalDateTime.now());
        intygsstatus.setPart(new Part());
        intygsstatus.getPart().setCode("recipient");
        intyg.getStatus().add(intygsstatus);

        UtlatandeMetaData result = converter.convert(intyg, false);
        assertNotNull(result);
        assertTrue(result.getStatuses().isEmpty());
    }

    private Intyg buildIntyg(final String intygsId, final String intygstyp, final String fullstandigtNamn, final String enhetsnamn,
            final LocalDateTime signeringstidpunkt) {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension(intygsId);
        intyg.setTyp(new TypAvIntyg());
        intyg.getTyp().setCode(intygstyp);
        intyg.setSkapadAv(new HosPersonal());
        intyg.getSkapadAv().setFullstandigtNamn(fullstandigtNamn);
        intyg.getSkapadAv().setEnhet(new Enhet());
        intyg.getSkapadAv().getEnhet().setEnhetsnamn(enhetsnamn);
        intyg.setSigneringstidpunkt(signeringstidpunkt);
        return intyg;
    }

}
