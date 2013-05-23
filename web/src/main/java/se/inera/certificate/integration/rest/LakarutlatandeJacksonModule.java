package se.inera.certificate.integration.rest;

import com.fasterxml.jackson.databind.module.SimpleModule;
import se.inera.certificate.model.Ovrigt;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeJacksonModule extends SimpleModule {

    public LakarutlatandeJacksonModule() {
        addSerializer(Ovrigt.class, new OvrigtSerializer());
    }
}
