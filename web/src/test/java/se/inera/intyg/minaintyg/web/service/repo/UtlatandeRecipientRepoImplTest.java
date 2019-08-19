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
package se.inera.intyg.minaintyg.web.service.repo;

import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.RecipientType;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.v1.utils.ResultTypeUtil;

@RunWith(MockitoJUnitRunner.class)
public class UtlatandeRecipientRepoImplTest {

    @Mock
    private ListKnownRecipientsResponderInterface wsInterface;

    @InjectMocks
    private UtlatandeRecipientRepoImpl repo = new UtlatandeRecipientRepoImpl();

    @Before
    public void setup() {
        when(wsInterface.listKnownRecipients(
            or(isNull(), anyString()),
            any())
        ).thenReturn(createResponse());

        repo.init();
    }

    private ListKnownRecipientsResponseType createResponse() {
        ListKnownRecipientsResponseType response = new ListKnownRecipientsResponseType();
        response.setResult(ResultTypeUtil.okResult());
        RecipientType rec = new RecipientType();
        rec.setId("FKASSA");
        rec.setName("Försäkringskassan");
        response.getRecipient().add(rec);
        return response;
    }

    @Test
    public void testGetAllRecipients() {
        assertTrue(repo.getAllRecipients().size() == 1);
    }
}
