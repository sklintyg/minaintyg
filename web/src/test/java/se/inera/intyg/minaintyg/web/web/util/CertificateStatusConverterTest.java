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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.minaintyg.web.api.CertificateStatus;

public class CertificateStatusConverterTest {

    @Test
    public void testToCertificateStatusNull() {
        CertificateStatus res = CertificateStatusConverter.toCertificateStatus(null);

        assertNotNull(res);
        assertTrue(res.getStatuses().isEmpty());
        assertFalse(res.getCancelled());
    }

    @Test
    public void testToCertificateStatusEmptyList() {
        CertificateStatus res = CertificateStatusConverter.toCertificateStatus(new ArrayList<>());

        assertNotNull(res);
        assertTrue(res.getStatuses().isEmpty());
        assertFalse(res.getCancelled());
    }

    @Test
    public void testToCertificateStatusCancelled() {
        List<Status> statuses = new ArrayList<>();
        statuses.add(buildStatus(CertificateState.CANCELLED));
        CertificateStatus res = CertificateStatusConverter.toCertificateStatus(statuses);

        assertNotNull(res);
        assertEquals(1, res.getStatuses().size());
        assertEquals(CertificateState.CANCELLED, res.getStatuses().get(0).getType());
        assertTrue(res.getCancelled());
    }

    @Test
    public void testToCertificateStatusSent() {
        List<Status> statuses = new ArrayList<>();
        statuses.add(buildStatus(CertificateState.SENT));
        CertificateStatus res = CertificateStatusConverter.toCertificateStatus(statuses);

        assertNotNull(res);
        assertEquals(1, res.getStatuses().size());
        assertEquals(CertificateState.SENT, res.getStatuses().get(0).getType());
        assertFalse(res.getCancelled());
    }

    @Test
    public void testToCertificateStatusMultiple() {
        List<Status> statuses = new ArrayList<>();
        statuses.add(buildStatus(CertificateState.SENT));
        statuses.add(buildStatus(CertificateState.CANCELLED));
        CertificateStatus res = CertificateStatusConverter.toCertificateStatus(statuses);

        assertNotNull(res);
        assertEquals(2, res.getStatuses().size());
        assertEquals(CertificateState.SENT, res.getStatuses().get(0).getType());
        assertEquals(CertificateState.CANCELLED, res.getStatuses().get(1).getType());
        assertTrue(res.getCancelled());
    }

    @Test
    public void testToCertificateStatusFilteresIrrelevantStatuses() {
        List<Status> statuses = new ArrayList<>();
        statuses.add(buildStatus(CertificateState.SENT));
        statuses.add(buildStatus(CertificateState.UNHANDLED));
        statuses.add(buildStatus(CertificateState.DELETED));
        statuses.add(buildStatus(CertificateState.IN_PROGRESS));
        statuses.add(buildStatus(CertificateState.PROCESSED));
        statuses.add(buildStatus(CertificateState.RECEIVED));
        statuses.add(buildStatus(CertificateState.RESTORED));
        statuses.add(buildStatus(CertificateState.CANCELLED));
        CertificateStatus res = CertificateStatusConverter.toCertificateStatus(statuses);

        assertNotNull(res);
        assertEquals(2, res.getStatuses().size());
        assertEquals(CertificateState.SENT, res.getStatuses().get(0).getType());
        assertEquals(CertificateState.CANCELLED, res.getStatuses().get(1).getType());
        assertTrue(res.getCancelled());
    }

    private Status buildStatus(CertificateState type) {
        Status status = new Status();
        status.setType(type);
        return status;
    }
}
