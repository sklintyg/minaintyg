package se.inera.intyg.minaintyg.user;

import java.util.Optional;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

public interface MinaIntygUserService {

  Optional<MinaIntygUser> getUser();
}
