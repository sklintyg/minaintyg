package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@Component
public class CitizenResponseConverter {

  private static final Map<StatusDTO, Status> STATUS_MAP = Map.of(
      StatusDTO.FOUND, Status.FOUND,
      StatusDTO.NOT_FOUND, Status.NOT_FOUND,
      StatusDTO.ERROR, Status.ERROR
  );

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