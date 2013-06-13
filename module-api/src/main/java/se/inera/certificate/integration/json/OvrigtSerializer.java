package se.inera.certificate.integration.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import se.inera.certificate.model.Ovrigt;

/**
 * Serializes an Ovrigt instance to JSON.
 * Simply takes the 'data' string and includes it as raw JSON data.
 *
 * @author andreaskaltenbach
 */
public class OvrigtSerializer extends StdScalarSerializer<Ovrigt> {

    public OvrigtSerializer() { super(Ovrigt.class); }

    @Override
    public void serialize(Ovrigt ovrigt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (ovrigt.getData() == null) {
            jsonGenerator.writeRawValue("{}");
        }
        else {
            jsonGenerator.writeRawValue(ovrigt.getData());
        }
    }
}
