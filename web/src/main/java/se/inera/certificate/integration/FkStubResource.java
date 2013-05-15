package se.inera.certificate.integration;

import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.integration.stub.FkMedicalCertificatesStore;

import com.google.common.collect.Maps;

@Path("/fk")
@Transactional
public class FkStubResource {

    private static final String [] KEYS = {"Personnummer", "Makulerad"};
    
    @Autowired
    private FkMedicalCertificatesStore fkMedicalCertificatesStore;

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public int count() {
        return fkMedicalCertificatesStore.getCount();
    }

    @GET
    @Path("/certificates")
    @Produces(MediaType.TEXT_HTML)
    public String certificates() {
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html><html><head>");
        sb.append("<link href='//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css' rel='stylesheet'>");
        sb.append("</head><body><div class='container'>");
        sb.append("<form method='POST' action='clear'><input type='submit' value='Clear'></form>");
        sb.append("<table class='table table-striped'>");
        sb.append("<thead><tr>");
        sb.append("<td>Id</td>");
        for(String key : KEYS) {
            sb.append("<td>").append(key).append("</td>");            
        }
        sb.append("</tr></thead>");
        for(Entry<String,Map<String,String>> e : fkMedicalCertificatesStore.getAll().entrySet()) {
            sb.append("<tr>");
            sb.append("<td>").append(e.getKey()).append("</td>");
            for(String key : KEYS) {
                sb.append("<td>").append(e.getValue().get(key)).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</div></body>");
        return sb.toString();
    }
    @GET
    @Path("/certificates")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Map<String,String>> certificatesJson() {
        return Maps.newHashMap(fkMedicalCertificatesStore.getAll());
    }    

    @POST
    @Path("/clear")
    @Produces(MediaType.TEXT_HTML)
    public String clear() {
        fkMedicalCertificatesStore.clear();
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head>");
        sb.append("<meta http-equiv='refresh' content='0;url=certificates'>");
        sb.append("</head></html>");
        return sb.toString();
    }

    @POST
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,String> clearJson() {
        fkMedicalCertificatesStore.clear();
        Map<String, String> m = Maps.newHashMap();
        m.put("result", "ok");
        return m;
    }
}
