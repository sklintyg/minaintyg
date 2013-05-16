package se.inera.certificate.integration.stub;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FkMedicalCertificatesStore {

    private ConcurrentHashMap<String, Map<String,String>> certificates = new ConcurrentHashMap<String,Map<String,String>>();
    
    public void addCertificate(String id, Map<String,String> props) {
        certificates.put(id, props);
    }

    public int getCount() {
        return certificates.size();
    }
    
    public Map<String,Map<String,String>> getAll() {
        return new HashMap<String,Map<String,String>>(certificates);
    }

    public void clear() {
        certificates.clear();
    }

    public void makulera(String id) {
        Map<String,String> m = Maps.newHashMap(certificates.get(id));
        m.put("Makulerad", "JA");
        certificates.put(id, m);
    }
}
