/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.api;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateRelation;
import se.inera.intyg.minaintyg.web.service.dto.CertificateEvent;

public class CertificateMetaTest {

    @Test
    public void testGetEventsKompletterat() {
        String fromIntygId = "123";
        String toIntygId = "321";

        List<CertificateRelation> relations = new ArrayList<>();
        relations.add(new CertificateRelation(fromIntygId,toIntygId,RelationKod.KOMPLT, LocalDateTime.now()));

        CertificateMeta cm = new CertificateMeta();
        cm.setId(toIntygId);
        cm.setRelations(relations);
        List<CertificateEvent> res = cm.getEvents();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(CertificateMeta.EVENT_TYPE_KOMPLETTERAT, res.get(0).getType());
    }

    @Test
    public void testGetEventsKompletterar() {
        String fromIntygId = "123";
        String toIntygId = "321";

        List<CertificateRelation> relations = new ArrayList<>();
        relations.add(new CertificateRelation(fromIntygId, toIntygId, RelationKod.KOMPLT, LocalDateTime.now()));

        CertificateMeta cm = new CertificateMeta();
        cm.setId(fromIntygId);
        cm.setRelations(relations);
        List<CertificateEvent> res = cm.getEvents();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(CertificateMeta.EVENT_TYPE_KOMPLETTERAR, res.get(0).getType());
    }
}