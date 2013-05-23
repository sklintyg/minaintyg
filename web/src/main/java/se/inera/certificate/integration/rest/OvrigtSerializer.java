package se.inera.certificate.integration.rest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import se.inera.certificate.model.Ovrigt;

import java.io.IOException;

/**
 * @author andreaskaltenbach
 */
public class OvrigtSerializer extends StdScalarSerializer<Ovrigt> {

    public OvrigtSerializer() { super(Ovrigt.class); }

    @Override
    public void serialize(Ovrigt ovrigt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(":" + ovrigt.getData());
    }
}
