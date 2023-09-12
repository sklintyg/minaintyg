package se.inera.intyg.minaintyg.user;

import static se.inera.intyg.minaintyg.user.UserToolkit.getUserFromPrincipal;

import java.util.Optional;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

@Service
public class MinaIntygUserServiceImpl implements MinaIntygUserService {

  @Override
  public Optional<MinaIntygUser> getUser() {
    return getUserFromPrincipal();
  }
}
