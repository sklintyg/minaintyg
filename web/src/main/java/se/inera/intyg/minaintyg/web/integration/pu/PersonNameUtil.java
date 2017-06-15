package se.inera.intyg.minaintyg.web.integration.pu;

import com.google.common.base.Strings;

import se.inera.intyg.infra.integration.pu.model.Person;

/**
 * Created by eriklupander on 2017-04-25.
 */
public class PersonNameUtil {

    public String buildFullName(Person person) {
        StringBuilder buf = new StringBuilder("");
        if (!Strings.isNullOrEmpty(person.getFornamn())) {
            buf.append(person.getFornamn()).append(" ");
        }
        if (!Strings.isNullOrEmpty(person.getMellannamn())) {
            buf.append(person.getMellannamn()).append(" ");
        }
        if (!Strings.isNullOrEmpty(person.getEfternamn())) {
            buf.append(person.getEfternamn()).append(" ");
        }
        return buf.toString().trim();
    }
}
