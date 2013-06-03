package se.inera.certificate.integration.stub;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class FkMedicalCertificatesStoreTest {

    private FkMedicalCertificatesStore store = null;
    
    @Before
    public void before() {
        store = new FkMedicalCertificatesStore();

        Map<String, String> properties = newHashMap();
        properties.put("Personnummer", "19121212-1212");
        properties.put("Makulerad", "NEJ");
        store.addCertificate("id-0001", newHashMap(properties));

        properties.clear();
        properties.put("Personnummer", "19121212-2222");
        properties.put("Makulerad", "JA");
        store.addCertificate("id-0002", newHashMap(properties));
    }
    
    @Test
    public void testGetCount() throws Exception {
        assertEquals(2, store.getCount());
    }
    
    @Test
    public void testGetAll() throws Exception {
        Map<String, Map<String,String>> all = store.getAll();
        assertEquals(2, all.size());
    }
    
    @Test
    public void testMakulera() throws Exception {
        store.makulera("id-0001");
        assertEquals("JA", store.getAll().get("id-0001").get("Makulerad"));
    }
    
    @Test
    public void testClear() throws Exception {
        store.clear();
        assertEquals(0, store.getCount());
    }
}
