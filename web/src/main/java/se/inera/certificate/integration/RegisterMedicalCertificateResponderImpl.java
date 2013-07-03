package se.inera.certificate.integration;

import static se.inera.certificate.integration.ResultOfCallUtil.okResult;
import intyg.registreraintyg._1.RegistreraIntygResponderInterface;

import javax.xml.ws.Holder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.integration.converter.LakarutlatandeTypeToLakarutlatandeConverter;
import se.inera.certificate.integration.v1.Lakarutlatande;
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

        Lakarutlatande lakarutlatande = LakarutlatandeTypeToLakarutlatandeConverter.convert(request.getLakarutlatande());
        registreraIntygResponder.registreraIntyg(new Holder<>(lakarutlatande));

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(okResult());
        return response;
    }
}
