package se.inera.certificate.integration;

import intyg.skickaintyg._1.SkickaIntygResponderInterface;
import org.apache.cxf.annotations.SchemaValidation;
import se.inera.certificate.integration.v1.LakarutlatandeType;

import javax.xml.ws.Holder;

/**
 * @author andreaskaltenbach
 */
@SchemaValidation
public class SkickaIntygResponder implements SkickaIntygResponderInterface {

    @Override
    public void skickaIntyg(Holder<LakarutlatandeType> intyg) {
        intyg.value.setId("hihiDe");

        // validate certificate

        // if invalid -> return validation errors

        // if valid -> proceed with storing the certificate along with its custom binary info


    }

    private void validate() {
        // send certificate to revalidate certificate agaist
    }
}
