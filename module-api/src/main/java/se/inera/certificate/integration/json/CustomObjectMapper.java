package se.inera.certificate.integration.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import se.inera.certificate.model.Ovrigt;

/**
 * Customized Jackson ObjectMapper for the inera-certificate projects.
 *
 * -registers additional serializers and deserializers for JODA date and time types
 * -registers a specialized serializer to represent certificate-specific data as JSON
 *
 * @author andreaskaltenbach
 */
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        super();

        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        registerModule(new Module());

    }

    private static final class Module extends SimpleModule {

        private Module() {
            addSerializer(Ovrigt.class, new OvrigtSerializer());

            addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

            addSerializer(LocalDate.class, new LocalDateSerializer());
            addDeserializer(LocalDate.class, new LocalDateDeserializer());
        }

    }
}
