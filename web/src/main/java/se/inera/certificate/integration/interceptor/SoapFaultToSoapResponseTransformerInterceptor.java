package se.inera.certificate.integration.interceptor;

import org.apache.cxf.feature.transform.AbstractXSLTInterceptor;
import org.apache.cxf.feature.transform.XSLTOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import se.inera.certificate.integration.xslt.ClasspathUriResolver;

import javax.xml.transform.TransformerFactory;
import java.lang.reflect.Field;

/**
 * CXF interceptor which turns SOAP faults into valid SOAP responses.
 *
 * Transformation is performed using XSLTs which transform the <soap:Fault> element to a proper response element
 * containing a <result> element giving more specifics about the error.
 *
 * @author andreaskaltenbach
 */
public class SoapFaultToSoapResponseTransformerInterceptor extends XSLTOutInterceptor {

    static {
        try {
            // Configure the private TransformerFactory defined in AbstractXSLTInterceptor
            Field transformFactoryField  = AbstractXSLTInterceptor.class.getDeclaredField("TRANSFORM_FACTORIY");
            transformFactoryField.setAccessible(true);
            TransformerFactory transformerFactory = (TransformerFactory) transformFactoryField.get(null);
            transformerFactory.setURIResolver(new ClasspathUriResolver());
            transformFactoryField.setAccessible(false);
        } catch (NoSuchFieldException|IllegalAccessException e) {
            throw new RuntimeException("Failed to set UriResolver for TransactionFactory", e);
        }
    }

    public static final int HTTP_OK = 200;

    public SoapFaultToSoapResponseTransformerInterceptor(String phase) {
        super(phase);
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        // switch HTTP status from 500 (internal server error) to 200 (ok)
        message.getExchange().getOutFaultMessage().put(Message.RESPONSE_CODE, HTTP_OK);

        super.handleMessage(message);
    }
}
