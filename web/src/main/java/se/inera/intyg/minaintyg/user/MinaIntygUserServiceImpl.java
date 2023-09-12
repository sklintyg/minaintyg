package se.inera.intyg.minaintyg.user;

import static se.inera.intyg.minaintyg.user.UserToolkit.getUserFromPrincipal;

import java.util.Optional;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;

@Service
public class MinaIntygUserServiceImpl implements MinaIntygUserService {

  private static final String SPACE = " ";
  private static final String EMPTY = "";

  @Override
  public Optional<MinaIntygUser> getUser() {
    return getUserFromPrincipal();
  }

  @Override
  public MinaIntygUser buildUserFromPersonResponse(PersonResponse personResponse,
      LoginMethod loginMethod) {
    return buildUser(personResponse, loginMethod);
  }

  private MinaIntygUser buildUser(PersonResponse personResponse,
      LoginMethod loginMethod) {
    final var personId = personResponse.getPerson().getPersonnummer();
    final var personName = buildPersonName(personResponse.getPerson());
    return MinaIntygUser.builder()
        .personId(personId)
        .personName(personName)
        .loginMethod(loginMethod)
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
