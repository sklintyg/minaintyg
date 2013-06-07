package se.inera.certificate.integration.rest;

import java.util.Collections;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  Factory which creates {@link ModuleRestApi} instances to communicate with the Rest API of the certificate modules.
 *
 * @author andreaskaltenbach
 */
@Component
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
        ModuleRestApi endpoint = JAXRSClientFactory.create(host + "/" + type + "/api", ModuleRestApi.class, Collections.singletonList(jacksonJsonProvider));
        return endpoint;
    }
}
