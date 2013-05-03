package se.inera.certificate.integration;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class CustomJacksonJaxbJsonProviderTest {

    @Test
    public void testWrite() throws Exception {
        MessageBodyWriter<Object> p = new CustomJacksonJaxbJsonProvider();        
        Object obj = new Tester();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        p.writeTo(obj, Tester.class, null, new Annotation[0], new MediaType(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON), null, os);
        
        assertEquals("{\"d\":\"2013-05-03\"}", os.toString());
    }
    
    @Test
    public void testRead() throws Exception {
        MessageBodyReader<Object> p = new CustomJacksonJaxbJsonProvider();        
        ByteArrayInputStream is = new ByteArrayInputStream("{ \"d\" : \"2013-05-04\" }".getBytes("UTF-8"));
                
        Tester o = (Tester)p.readFrom(Object.class, Tester.class, new Annotation[0], new MediaType(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON), null, is);        
        
        assertEquals(new LocalDate("2013-05-04"), o.getD());
    }
    
    @XmlRootElement
    public static class Tester {
        
        @JsonDeserialize(using = com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer.class)
        @JsonSerialize(using = com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer.class)
        private LocalDate d;
        
        public Tester() {
            d = new LocalDate("2013-05-03");
        }
        
        public LocalDate getD() {
            return d;
        }
        @JsonSetter
        public void setD(LocalDate d) {
            this.d = d;
        }
    }
}
