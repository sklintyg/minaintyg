package se.inera.certificate.integration.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.Partial;
import se.inera.certificate.schema.adapter.PartialAdapter;

/**
 * @author andreaskaltenbach
 */
public class PartialSerializer extends StdSerializer<Partial> {

    public PartialSerializer() {
        super(Partial.class);
    }

    @Override
    public void serialize(Partial partial, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(PartialAdapter.printPartial(partial));
    }
}
