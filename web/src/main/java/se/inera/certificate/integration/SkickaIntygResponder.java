package se.inera.certificate.integration;

import intyg.skickaintyg._1.SkickaIntygResponderInterface;
import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import se.inera.certificate.integration.rest.ModuleRestApi;
import se.inera.certificate.integration.v1.Lakarutlatande;

import javax.xml.ws.Holder;

/**
 * @author andreaskaltenbach
 */
@SchemaValidation
public class SkickaIntygResponder implements SkickaIntygResponderInterface {

    private String host;


    public void setPort(String port) {
        host = "http://localhost:" + port;
    }

    @Override
    public void skickaIntyg(Holder<Lakarutlatande> lakarutlatande) {
        String type = lakarutlatande.value.getTyp();

        sendLakarutlatandeForValidation(type, lakarutlatande.value);

        // validate certificate

        // if invalid -> return validation errors

        // if valid -> proceed with storing the certificate along with its custom binary info
    }


    private void sendLakarutlatandeForValidation(String type, Lakarutlatande lakarutlatande) {


        ModuleRestApi endpoint = JAXRSClientFactory.create(host + "/" + type + "/api", ModuleRestApi.class);

        endpoint.validate(lakarutlatande);
    }


}
