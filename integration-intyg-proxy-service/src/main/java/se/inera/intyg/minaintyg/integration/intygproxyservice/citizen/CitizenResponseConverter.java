package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.citizen.model.Citizen;
import se.inera.intyg.minaintyg.integration.api.citizen.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.StatusDTO;

@Component
public class CitizenResponseConverter {

  private static final String SPACE = " ";
  private static final String EMPTY = "";
  private static final Map<StatusDTO, Status> STATUS_MAP = Map.of(
      StatusDTO.FOUND, Status.FOUND,
      StatusDTO.NOT_FOUND, Status.NOT_FOUND,
      StatusDTO.ERROR, Status.ERROR
  );

  public Citizen convertCitizen(CitizenDTO citizenDTO) {
    return Citizen.builder()
        .citizenId(citizenDTO.getPersonnummer())
        .name(buildCitizenName(citizenDTO))
        .build();
  }

  public Status convertStatus(StatusDTO statusDTO) {
    return STATUS_MAP.get(statusDTO);
  }

  private String buildCitizenName(CitizenDTO personDTO) {
    return personDTO.getFornamn()
        + SPACE
        + includeMiddleName(personDTO.getMellannamn())
        + personDTO.getEfternamn();
  }

  private String includeMiddleName(String middleName) {
    return middleName != null ? middleName + SPACE : EMPTY;
  }
}
