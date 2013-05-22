package se.inera.certificate.integration.stub;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.StyledEditorKit.ItalicAction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FkStubResourceTest {

    @Mock
    FkMedicalCertificatesStore store;

    @Mock
    Map<String, Map<String, String>> map;

    @Mock
    Set<Map.Entry<String, Map<String, String>>> entrySet;

    @Mock
    Iterator<Entry<String, Map<String, String>>> iterator;

    @InjectMocks
    private FkStubResource stub = new FkStubResource();

    @Mock
    private Entry<String, Map<String, String>> value;

    @Test
    public void testGetCount() throws Exception {
        when(store.getCount()).thenReturn(15);
        assertEquals(15, stub.count());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCertificates() throws Exception {
        when(store.getAll()).thenReturn(map);
        when(map.entrySet()).thenReturn(entrySet);
        when(entrySet.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(value);
        when(value.getKey()).thenReturn("1", "2", "3");

        Map<String, String> v1 = newHashMap();
        v1.put("Personnummer", "19121212-1111");
        v1.put("Makulerad", "NEJ");
        Map<String, String> v2 = newHashMap();
        v2.put("Personnummer", "19121212-2222");
        v2.put("Makulerad", "NEJ");
        Map<String, String> v3 = newHashMap();
        v3.put("Personnummer", "19121212-3333");
        v3.put("Makulerad", "JA");
        when(value.getValue()).thenReturn(v1, v1, v2, v2, v3, v3);

        String v1Str = "<tr><td>1</td><td>19121212-1111</td><td>NEJ</td></tr>";
        String v2Str = "<tr><td>2</td><td>19121212-2222</td><td>NEJ</td></tr>";
        String v3Str = "<tr><td>3</td><td>19121212-3333</td><td>JA</td></tr>";

        String result = stub.certificates();

        assertTrue(result.contains(v1Str));
        assertTrue(result.contains(v2Str));
        assertTrue(result.contains(v3Str));
    }

    @Test
    public void testCertificatesJson() throws Exception {

        Map<String, String> v1 = newHashMap();
        v1.put("Personnummer", "19121212-1111");
        v1.put("Makulerad", "NEJ");
        Map<String, String> v2 = newHashMap();
        v2.put("Personnummer", "19121212-2222");
        v2.put("Makulerad", "NEJ");
        Map<String, String> v3 = newHashMap();
        v3.put("Personnummer", "19121212-3333");
        v3.put("Makulerad", "JA");
        Map<String,Map<String,String>> expected = newHashMap();
        expected.put("1", v1);
        expected.put("2", v2);
        expected.put("3", v3);
        when(store.getAll()).thenReturn(expected);

        Map<String,Map<String,String>> result = stub.certificatesJson();

        assertEquals(expected, result);
    }
    
    @Test
    public void testClear() throws Exception {
        stub.clear();
        verify(store).clear();
    }
    @Test
    public void testClearJson() throws Exception {
        stub.clearJson();
        verify(store).clear();
    }

}
