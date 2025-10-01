package se.inera.intyg.minaintyg.user;

import static se.inera.intyg.minaintyg.user.UserToolkit.getUserFromPrincipal;

import java.util.Optional;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.integration.api.user.LoggedInMinaIntygUserService;
import se.inera.intyg.minaintyg.integration.api.user.model.LoggedInMinaIntygUser;

@Service
public class UserService implements LoggedInMinaIntygUserService {


  public Optional<MinaIntygUser> getLoggedInUser() {
    return getUserFromPrincipal();
  }

  @Override
  public LoggedInMinaIntygUser loggedInMinaIntygUser() {
    return getLoggedInUser()
        .map(user ->
            LoggedInMinaIntygUser.builder()
                .personId(user.getPersonId())
                .build()
        )
        .orElseThrow(() -> new IllegalStateException("No logged in user found"));
  }
}
