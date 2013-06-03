package se.inera.certificate.integration;

import intyg.registreraintyg._1.RegistreraIntygResponderInterface;

import java.util.Collections;

import javax.xml.ws.Holder;

import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.converter.LakarutlatandeJaxbToLakarutlatandeConverter;
import se.inera.certificate.integration.rest.ModuleRestApi;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.integration.validator.ValidationException;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Valideringsresultat;
import se.inera.certificate.service.CertificateService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * @author andreaskaltenbach
 */
@SchemaValidation
public class RegistreraIntygResponder implements RegistreraIntygResponderInterface {

    private String host;

    @Autowired
    private JacksonJaxbJsonProvider jacksonJsonProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CertificateService certificateService;

    public void setPort(String port) {
        host = "http://localhost:" + port;
    }

    @Override
    public void registreraIntyg(Holder<Lakarutlatande> lakarutlatande) {
        String type = lakarutlatande.value.getTyp();

        // let the certificate validate by the corresponding certificate module
        validate(type, lakarutlatande.value);

        String certificateExtension = extractCertificateExtensionData(type, lakarutlatande.value);

        se.inera.certificate.model.Lakarutlatande model  = LakarutlatandeJaxbToLakarutlatandeConverter.convert(lakarutlatande.value);

        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData(certificateExtension);
        model.setOvrigt(ovrigt );

        certificateService.storeCertificate(model);
    }

    private ModuleRestApi getModuleRestService(String type) {
        ModuleRestApi endpoint = JAXRSClientFactory.create(host + "/" + type + "/api", ModuleRestApi.class, Collections.singletonList(jacksonJsonProvider));
        return endpoint;
    }


    private void validate(String type, Lakarutlatande lakarutlatande) {

        ModuleRestApi endpoint = getModuleRestService(type);

        Valideringsresultat result = endpoint.validate(lakarutlatande);

        if (!result.getFel().isEmpty()) {
            throw new ValidationException(result.getFel());
        }
    }

    private String extractCertificateExtensionData(String type, Lakarutlatande lakarutlatande) {

        ModuleRestApi endpoint = getModuleRestService(type);

        Object certificateExtension = endpoint.extract(lakarutlatande);

        try {
            return objectMapper.writeValueAsString(certificateExtension);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON: " + certificateExtension, e);
        }
    }
}
