package se.inera.certificate.integration.stub;

import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificate;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponse;

/**
 * @author par.wenaker
 */
@Transactional
@SchemaValidation
public class RegisterMedicalCertificateResponderStub implements RegisterMedicalCertificateResponderInterface {

    private Logger logger = LoggerFactory.getLogger(RegisterMedicalCertificateResponderStub.class);

    @Autowired
    private FkMedicalCertificatesStore fkMedicalCertificatesStore;

    @Override
    public RegisterMedicalCertificateResponse registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificate request) {

        RegisterMedicalCertificateResponse response = new RegisterMedicalCertificateResponse();

        String id = request.getLakarutlatande().getLakarutlatandeId();

        Map<String, String> props = Maps.newHashMap();
        props.put("Personnummer", request.getLakarutlatande().getPatient().getPersonId().getExtension());
        props.put("Makulerad", "NEJ");
        logger.info("STUB Received request");
        fkMedicalCertificatesStore.addCertificate(id, props);

        return response;
    }
}
