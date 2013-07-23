package se.inera.certificate.integration.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

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
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        // trim string and replace all tabs and carriage returns
        String trimmed = value.replaceAll("\\s{2,}", " ").trim();
        standardSerializer.serialize(trimmed, jgen, provider);

    }
}
