package se.inera.certificate.integration;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;

@Transactional
@SchemaValidation
public class SendMedicalCertificateResponderImpl implements SendMedicalCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder;

    @Autowired
    private List<CertificateSupport> supportedCertificates;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SendMedicalCertificateResponseType sendMedicalCertificate(AttributedURIType logicalAddress, SendMedicalCertificateRequestType parameters) {
        SendMedicalCertificateResponseType response = new SendMedicalCertificateResponseType();

        String certificateId = parameters.getSend().getLakarutlatande().getLakarutlatandeId();
        String civicRegistrationNumber = parameters.getSend().getLakarutlatande().getPatient().getPersonId().getExtension();

        Certificate certificate = certificateService.getCertificate(civicRegistrationNumber, certificateId);

        if (certificate.getType().equalsIgnoreCase("fk7263")) {
            registerMedicalCertificateResponder.registerMedicalCertificate(logicalAddress, getJaxbObject(certificate));
            response.setResult(ResultOfCallUtil.okResult());
        } else {
            response.setResult(ResultOfCallUtil.applicationErrorResult("Metoden är inte implementerad"));
        }

        return response;
    }

    private RegisterMedicalCertificateType getJaxbObject(Certificate certificate) {
        try {
            RegisterMedicalCertificateType register = new RegisterMedicalCertificateType();
            Lakarutlatande lakarutlatande = objectMapper.readValue(certificate.getDocument(), Lakarutlatande.class);
            // TODO Kopiera
            register.setLakarutlatande(new LakarutlatandeType());
            register.getLakarutlatande().setLakarutlatandeId(lakarutlatande.getId());
            return register;
        } catch (Exception e) {
            // TODO: Kasta annat undantag! /PW
            throw new RuntimeException(e);
        }
    }
}
