package se.inera.intyg.minaintyg.web.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import autovalue.shaded.com.google$.common.base.$VerifyException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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