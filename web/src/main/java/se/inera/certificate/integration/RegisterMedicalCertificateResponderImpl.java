package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

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
        String careUnitName = request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivarnamn();
        String civicRegistrationNumber = request.getLakarutlatande().getPatient().getPersonId().getExtension();
        LocalDate signedDate = request.getLakarutlatande().getSigneringsdatum().toLocalDate();
        LocalDate validFromDate = extractValidFromDate(request.getLakarutlatande());
        LocalDate validToDate = extractValidToDate(request.getLakarutlatande());

        Certificate certificate = new Certificate(certificateId, document);
        CertificateMetaData metaData = new CertificateMetaData(certificate);
        metaData.setCareUnitName(careUnitName);
        metaData.setCivicRegistrationNumber(civicRegistrationNumber);
        metaData.setSignedDate(signedDate);
        metaData.setValidFromDate(validFromDate);
        metaData.setValidToDate(validToDate);

        // TODO - extract additional meta data from the certificate

        certificateService.storeCertificate(metaData);
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
