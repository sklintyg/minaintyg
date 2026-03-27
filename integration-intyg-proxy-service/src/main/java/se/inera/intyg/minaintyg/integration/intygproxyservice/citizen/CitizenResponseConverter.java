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
package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@Component
public class CitizenResponseConverter {

  private static final Map<StatusDTO, Status> STATUS_MAP =
      Map.of(
          StatusDTO.FOUND, Status.FOUND,
          StatusDTO.NOT_FOUND, Status.NOT_FOUND,
          StatusDTO.ERROR, Status.ERROR);

  public Person convertCitizen(CitizenDTO citizenDTO) {
    return Person.builder()
        .personId(citizenDTO.getPersonnummer())
        .name(buildCitizenName(citizenDTO))
        .isActive(citizenDTO.isActive())
        .build();
  }

  public Status convertStatus(StatusDTO statusDTO) {
    return STATUS_MAP.get(statusDTO);
  }

  private String buildCitizenName(CitizenDTO personDTO) {
    return "%s %s".formatted(personDTO.getFornamn(), personDTO.getEfternamn());
  }
}
