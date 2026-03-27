/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.testability;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestPersonService {

  public List<TestPerson> getPersons() {
    return listOfTestPersons();
  }

  private List<TestPerson> listOfTestPersons() {
    return List.of(
        TestPerson.builder().personId("194011306125").personName("Athena React Andersson ").build(),
        TestPerson.builder()
            .personId("198901192396")
            .personName("Agnarsson React Agnarsson")
            .build(),
        TestPerson.builder().personId("200210282398").personName("Albert React Albertsson").build(),
        TestPerson.builder().personId("200210292389").personName("Albertina React Alison").build(),
        TestPerson.builder().personId("197901242391").personName("Albin React Ander").build(),
        TestPerson.builder().personId("194110299221").personName("Alexa React Valfridsson").build(),
        TestPerson.builder().personId("197901252382").personName("Aline React Andersson").build(),
        TestPerson.builder().personId("199606282391").personName("Allan React Allanson").build(),
        TestPerson.builder().personId("199606292382").personName("Alma React Almarsson").build(),
        TestPerson.builder().personId("194112128154").personName("Alve React Alfredsson").build());
  }
}
