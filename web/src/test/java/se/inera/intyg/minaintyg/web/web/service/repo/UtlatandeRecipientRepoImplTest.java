package se.inera.intyg.minaintyg.web.web.service.repo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.RecipientType;
import se.inera.intyg.common.fk7263.schemas.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtlatandeRecipientRepoImplTest {

    @Mock
    ListKnownRecipientsResponderInterface wsInterface;

    @InjectMocks
    private UtlatandeRecipientRepoImpl repo = new UtlatandeRecipientRepoImpl();

    @Before
    public void setup() {
        when(wsInterface.listKnownRecipients(anyString(), any()))
                .thenReturn(createResponse());
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
