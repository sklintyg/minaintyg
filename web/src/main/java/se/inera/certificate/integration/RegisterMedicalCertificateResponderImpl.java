package se.inera.certificate.integration;

import javax.xml.ws.Holder;

import static se.inera.certificate.integration.util.ResultOfCallUtil.okResult;

import intyg.registreraintyg._1.RegistreraIntygResponderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.integration.converter.LakarutlatandeTypeToUtlatandeConverter;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author andreaskaltenbach
 */
@Transactional
public class RegisterMedicalCertificateResponderImpl implements RegisterMedicalCertificateResponderInterface {

    @Autowired
    private RegistreraIntygResponderInterface registreraIntygResponder;

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificateType request) {

        Utlatande utlatande = LakarutlatandeTypeToUtlatandeConverter.convert(request.getLakarutlatande());
        registreraIntygResponder.registreraIntyg(new Holder<>(utlatande));

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(okResult());
        return response;
    }
}
