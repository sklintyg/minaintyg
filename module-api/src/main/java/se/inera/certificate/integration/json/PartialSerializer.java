package se.inera.certificate.integration.json;

import java.io.IOException;

import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.monthOfYear;
import static org.joda.time.format.ISODateTimeFormat.yearMonth;
import static org.joda.time.format.ISODateTimeFormat.yearMonthDay;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.Partial;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author andreaskaltenbach
 */
public class PartialSerializer extends StdSerializer<Partial> {

    public PartialSerializer() {
        super(Partial.class);
    }

    @Override
    public void serialize(Partial partial, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(partialString(partial));
    }

    private String partialString(Partial partial) {
        if (!partial.isSupported(dayOfMonth()) && !partial.isSupported(monthOfYear())) {
            return partial.toString(ISODateTimeFormat.year());
        }

        if (!partial.isSupported(dayOfMonth())) {
            return partial.toString(yearMonth());
        }

        return partial.toString(yearMonthDay());
    }
}
