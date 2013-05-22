package se.inera.certificate.integration;

import intyg.skickaintyg._1.SkickaIntygResponderInterface;
import se.inera.certificate.integration.v1.IntygType;

import javax.xml.ws.Holder;

/**
 * @author andreaskaltenbach
 */
public class SkickaIntygResponder implements SkickaIntygResponderInterface {

    @Override
    public void skickaIntyg(Holder<IntygType> intyg) {
        intyg.value.getLakarutlatande().setId("hihiDe");
    }
}
