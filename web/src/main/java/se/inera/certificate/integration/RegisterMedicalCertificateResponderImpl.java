package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

/**
 * @author andreaskaltenbach
 */
@Transactional
@SchemaValidation
public class RegisterMedicalCertificateResponderImpl implements RegisterMedicalCertificateResponderInterface {

    public static final String FK_7263 = "fk7263";
    @Autowired
    private CertificateService certificateService;

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificateType request) {

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        // unmarshal the certificate document
        String document;
        try {
            document = marshalCertificate(request);
        } catch (JAXBException e) {
            response.setResult(failResult("Unable to marshal certificate information"));
            return response;
        }

        String certificateId = request.getLakarutlatande().getLakarutlatandeId();

        VardgivareType vardgivare = request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare();
        String careUnitName = (vardgivare != null) ? vardgivare.getVardgivarnamn() : null;

        String civicRegistrationNumber = request.getLakarutlatande().getPatient().getPersonId().getExtension();

        LocalDate signedDate = request.getLakarutlatande().getSigneringsdatum().toLocalDate();
        String signedDoctorName = request.getLakarutlatande().getSkapadAvHosPersonal().getFullstandigtNamn();

        LocalDate validFromDate = extractValidFromDate(request.getLakarutlatande());
        LocalDate validToDate = extractValidToDate(request.getLakarutlatande());

        Certificate certificate = new Certificate(certificateId, document);
        certificate.setCareUnitName(careUnitName);
        certificate.setCivicRegistrationNumber(civicRegistrationNumber);
        certificate.setSignedDate(signedDate);
        certificate.setSigningDoctorName(signedDoctorName);
        certificate.setValidFromDate(validFromDate);
        certificate.setValidToDate(validToDate);
        certificate.setType(FK_7263);

        certificateService.storeCertificate(certificate);
        response.setResult(okResult());
        return response;
    }

    private static LocalDate extractValidFromDate(FunktionstillstandType funktionstillstand) {

        if (funktionstillstand.getArbetsformaga() == null) {
            return null;
        }

        LocalDate fromDate = null;
        for (ArbetsformagaNedsattningType arbetsformagaNedsattning : funktionstillstand.getArbetsformaga().getArbetsformagaNedsattning()) {
            if (fromDate == null || fromDate.isAfter(arbetsformagaNedsattning.getVaraktighetFrom())) {
                fromDate = arbetsformagaNedsattning.getVaraktighetFrom();
            }
        }
        return fromDate;
    }

    private static LocalDate extractValidFromDate(LakarutlatandeType lakarutlatande) {

        LocalDate fromDate = null;
        for (FunktionstillstandType funktionstillstand : lakarutlatande.getFunktionstillstand()) {
            LocalDate funktionstillstandFromDate = extractValidFromDate(funktionstillstand);
            if (fromDate == null || fromDate.isAfter(funktionstillstandFromDate)) {
                fromDate = funktionstillstandFromDate;
            }
        }
        return fromDate;
    }

    private static LocalDate extractValidToDate(FunktionstillstandType funktionstillstand) {

        if (funktionstillstand.getArbetsformaga() == null) {
            return null;
        }

        LocalDate fromDate = null;
        for (ArbetsformagaNedsattningType arbetsformagaNedsattning : funktionstillstand.getArbetsformaga().getArbetsformagaNedsattning()) {
            if (fromDate == null || fromDate.isBefore(arbetsformagaNedsattning.getVaraktighetTom())) {
                fromDate = arbetsformagaNedsattning.getVaraktighetTom();
            }
        }
        return fromDate;
    }

    private static LocalDate extractValidToDate(LakarutlatandeType lakarutlatande) {

        LocalDate fromDate = null;
        for (FunktionstillstandType funktionstillstand : lakarutlatande.getFunktionstillstand()) {
            LocalDate funktionstillstandFromDate = extractValidToDate(funktionstillstand);
            if (fromDate == null || fromDate.isBefore(funktionstillstandFromDate)) {
                fromDate = funktionstillstandFromDate;
            }
        }
        return fromDate;
    }

    private String marshalCertificate(RegisterMedicalCertificateType request) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        StringWriter stringWriter = new StringWriter();

        JAXBElement<RegisterMedicalCertificateType> jaxbElement = new ObjectFactory().createRegisterMedicalCertificate(request);

        marshaller.marshal(jaxbElement, stringWriter);

        return stringWriter.toString();
    }
}
