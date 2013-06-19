package se.inera.certificate.integration.interceptor;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.Phase;
import se.inera.certificate.integration.validator.LakarutlatandeValidator;
import se.inera.certificate.integration.validator.ValidationException;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificate;


/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeValidatingInterceptor extends AbstractSoapInterceptor {

    public LakarutlatandeValidatingInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {

        RegisterMedicalCertificate registerMedicalCertificate = (RegisterMedicalCertificate) MessageContentsList.getContentsList(message).get(1);
        Lakarutlatande lakarutlatande = registerMedicalCertificate.getLakarutlatande();

        try {
            new LakarutlatandeValidator(lakarutlatande).validate();
        } catch (ValidationException ex) {
            throw new SoapFault(ex.getMessage(), ex, Fault.FAULT_CODE_CLIENT);
        }

    }
}
