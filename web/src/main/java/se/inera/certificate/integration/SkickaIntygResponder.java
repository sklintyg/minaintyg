package se.inera.certificate.integration;

import intyg.skickaintyg._1.SkickaIntygResponderInterface;
import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import se.inera.certificate.integration.v1.LakarutlatandeType;

import javax.xml.ws.Holder;

/**
 * @author andreaskaltenbach
 */
@SchemaValidation
public class SkickaIntygResponder implements SkickaIntygResponderInterface {

    @Override
    public void skickaIntyg(Holder<LakarutlatandeType> lakarutlatande) {
        String type = lakarutlatande.value.getTyp();

        sendLakarutlatandeForValidation(type, lakarutlatande.value);

        // validate certificate

        // if invalid -> return validation errors

        // if valid -> proceed with storing the certificate along with its custom binary info
    }


    private void sendLakarutlatandeForValidation(String type, LakarutlatandeType lakarutlatande) {


        ModuleRestApi endpoint = JAXRSClientFactory.create("http://localhost/" + type, ModuleRestApi.class);

        endpoint.validate(new LakarutlatandeType());
    }


}
