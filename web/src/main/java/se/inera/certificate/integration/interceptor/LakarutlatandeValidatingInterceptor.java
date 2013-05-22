package se.inera.certificate.integration.interceptor;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.Phase;
import se.inera.certificate.integration.validator.LakarutlatandeValidator;
import se.inera.certificate.integration.validator.ValidationException;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;


/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeValidatingInterceptor extends AbstractSoapInterceptor {

    public LakarutlatandeValidatingInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {

        RegisterMedicalCertificateType registerMedicalCertificate = (RegisterMedicalCertificateType) MessageContentsList.getContentsList(message).get(1);
        LakarutlatandeType lakarutlatande = registerMedicalCertificate.getLakarutlatande();

        try {
            new LakarutlatandeValidator(lakarutlatande).validate();
        } catch (ValidationException ex) {
            throw new SoapFault(ex.getMessage(), Fault.FAULT_CODE_CLIENT);
        }

    }
}
