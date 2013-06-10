package se.inera.certificate.integration.rest;

import java.util.Collections;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 *  Factory which creates {@link ModuleRestApi} instances to communicate with the Rest API of the certificate modules.
 *
 * @author andreaskaltenbach
 */
public class ModuleRestApiFactory {

    private String host;

    @Autowired
    private JacksonJaxbJsonProvider jacksonJsonProvider;

    public void setPort(String port) {
        host = "http://localhost:" + port;
    }

    /**
     * Creates a {@link ModuleRestApi} for the given certificate type.
     */
    public ModuleRestApi getModuleRestService(String type) {
        String uri = host + "/" + type + "/api";
        ModuleRestApi endpoint = JAXRSClientFactory.create(uri, ModuleRestApi.class, Collections.singletonList(jacksonJsonProvider));
        return endpoint;
    }
}
