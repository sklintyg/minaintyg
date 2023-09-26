package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@Component
public class PersonSvarConverter {

  private static final String SPACE = " ";
  private static final String EMPTY = "";
  private static final Map<StatusDTO, Status> STATUS_MAP = Map.of(
      StatusDTO.FOUND, Status.FOUND,
      StatusDTO.NOT_FOUND, Status.NOT_FOUND,
      StatusDTO.ERROR, Status.ERROR
  );

  public Person convertPerson(PersonDTO personDTO) {
    return Person.builder()
        .personId(personDTO.getPersonnummer())
        .name(buildPersonName(personDTO))
        .build();
  }

  public Status convertStatus(StatusDTO statusDTO) {
    return STATUS_MAP.get(statusDTO);
  }

  private String buildPersonName(PersonDTO personDTO) {
    return personDTO.getFornamn()
        + SPACE
        + includeMiddleName(personDTO.getMellannamn())
        + personDTO.getEfternamn();
  }

  private String includeMiddleName(String middleName) {
    return middleName != null ? middleName + SPACE : EMPTY;
  }
}
