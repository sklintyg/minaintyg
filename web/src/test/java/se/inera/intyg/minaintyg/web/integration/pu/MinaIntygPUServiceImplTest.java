/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.infra.integration.pu.model.PersonSvar;
import se.inera.intyg.infra.integration.pu.services.PUService;
import se.inera.intyg.minaintyg.web.exception.PUServiceErrorException;
import se.inera.intyg.minaintyg.web.exception.PersonNotFoundException;
import se.inera.intyg.schemas.contract.Personnummer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by eriklupander on 2017-04-25.
 */
@RunWith(MockitoJUnitRunner.class)
public class MinaIntygPUServiceImplTest {

    private static final String PERSON_ID = "19121212-1212";
    private Personnummer pnr = new Personnummer(PERSON_ID);

    @Mock
    private PUService puService;

    @InjectMocks
    private MinaIntygPUServiceImpl testee = new MinaIntygPUServiceImpl();

    @Test
    public void testGetPersonOk() {
        when(puService.getPerson(pnr)).thenReturn(buildOkPUSvar(PersonSvar.Status.FOUND));
        Person person = testee.getPerson(PERSON_ID);
        assertEquals(pnr.getPersonnummerWithoutDash(), person.getPersonnummer().getPersonnummerWithoutDash());
    }

    @Test(expected = PersonNotFoundException.class)
    public void testGetPersonNotFoundStopsLoggingIn() {
        when(puService.getPerson(pnr)).thenReturn(buildOkPUSvar(PersonSvar.Status.NOT_FOUND));
        testee.getPerson(PERSON_ID);
    }

    @Test(expected = PUServiceErrorException.class)
    public void testGetPersonErrorStopsLoggingIn() {
        when(puService.getPerson(pnr)).thenReturn(buildOkPUSvar(PersonSvar.Status.ERROR));
        testee.getPerson(PERSON_ID);
    }

    private PersonSvar buildOkPUSvar(PersonSvar.Status status) {
        Person person = new Person(pnr, false, false, "", "", "", "" ,"" ,"");
        return new PersonSvar(person, status);
    }
}
