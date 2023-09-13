package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.Person;

@Service
public class PersonConverterServiceImpl implements PersonConverterService {

  private static final String SPACE = " ";
  private static final String EMPTY = "";

  @Override
  public Person convert(Person person) {
    return Person.builder()
        .namn(buildPersonName(person))
        .personnummer(person.getPersonnummer())
        .build();
  }

  private String buildPersonName(Person person) {
    return person.getFornamn()
        + SPACE
        + includeMiddleName(person.getMellannamn())
        + person.getEfternamn();
  }

  private String includeMiddleName(String middleName) {
    return middleName != null ? middleName + SPACE : EMPTY;
  }
}
