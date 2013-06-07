package se.inera.certificate.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import intyg.registreraintyg._1.RegistreraIntygResponderInterface;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.integration.converter.LakarutlatandeJaxbToLakarutlatandeConverter;
import se.inera.certificate.integration.rest.ModuleRestApi;
import se.inera.certificate.integration.rest.ModuleRestApiFactory;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.integration.validator.ValidationException;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.service.CertificateService;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.xml.ws.Holder;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author andreaskaltenbach
 */
@SchemaValidation
public class RegistreraIntygResponder implements RegistreraIntygResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistreraIntygResponder.class);

    @Autowired
    private ModuleRestApiFactory moduleRestApiFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CertificateService certificateService;

    @Override
    public void registreraIntyg(Holder<Lakarutlatande> lakarutlatande) {
        String type = lakarutlatande.value.getTyp();

        // let the certificate validate by the corresponding certificate module
        validate(type, lakarutlatande.value);

        String certificateExtension = extractCertificateExtensionData(type, lakarutlatande.value);

        se.inera.certificate.model.Lakarutlatande model = LakarutlatandeJaxbToLakarutlatandeConverter.convert(lakarutlatande.value);

        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData(certificateExtension);
        model.setOvrigt(ovrigt);

        certificateService.storeCertificate(model);
    }

    private void validate(String type, Lakarutlatande lakarutlatande) {

        ModuleRestApi endpoint = moduleRestApiFactory.getModuleRestService(type);

        Response response = endpoint.validate(lakarutlatande);

        switch (response.getStatus()) {
            case 400: // BadRequest -> extract validation error message and transform to ValidationException
                InputStream inputStream = (InputStream) response.getEntity();

                try {
                    String validationErrorMessage = IOUtils.toString(inputStream);
                    throw new ValidationException(validationErrorMessage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read response for validation of '" + type + "' certificate.");
                }

            case 200:
                break;
            default:
                String errorMessage = "Failed to validate certificate for certificate type '" + type + "'. HTTP status code is " + response.getStatus();
                LOGGER.error(errorMessage);
                throw new RuntimeException(errorMessage);
        }
    }

    private String extractCertificateExtensionData(String type, Lakarutlatande lakarutlatande) {

        ModuleRestApi endpoint = moduleRestApiFactory.getModuleRestService(type);

        try {
            Object certificateExtension = endpoint.extract(lakarutlatande);

            try {
                return objectMapper.writeValueAsString(certificateExtension);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process JSON: " + certificateExtension, e);
            }

        } catch (NotFoundException e) {
            LOGGER.error("No certificate module available to extract specifics for certificate type " + type);
            throw new RuntimeException("No certificate module available to extract specifics for certificate type " + type, e);
        }
    }
}
