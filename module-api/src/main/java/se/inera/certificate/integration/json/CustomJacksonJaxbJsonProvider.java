package se.inera.certificate.integration.json;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class CustomJacksonJaxbJsonProvider extends JacksonJaxbJsonProvider {

    public CustomJacksonJaxbJsonProvider() {
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
