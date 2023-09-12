package se.inera.intyg.minaintyg.user;

import java.util.Optional;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;

public interface MinaIntygUserService {

  Optional<MinaIntygUser> getUser();

  MinaIntygUser buildUserFromPersonResponse(PersonResponse personResponse, LoginMethod loginMethod);
}
