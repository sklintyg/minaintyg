/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
