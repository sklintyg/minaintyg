package se.inera.certificate.integration.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import java.io.IOException;

/**
 * Specialized Jackson {@link com.fasterxml.jackson.databind.JsonSerializer} for strings.
 *
 * Removes unnecessary whitespaces, tabs and carriage returns.
 *
 * @author andreaskaltenbach
 */
public class TrimmingStringSerializer extends StdScalarSerializer<String> {

    public TrimmingStringSerializer() {
        super(String.class);
    }

    private StringSerializer standardSerializer = new StringSerializer();

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

        // trim string and replace all tabs and carriage returns
        standardSerializer.serialize(value
                .replaceAll("\\n", " ")
                .replaceAll("\\t", " ")
                .replaceAll("\\s{1,}", " ")
                .trim(), jgen, provider);

    }
}
