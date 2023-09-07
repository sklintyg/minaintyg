package se.inera.intyg.minaintyg.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

@Service
public class MinaIntygUserServiceImpl implements MinaIntygUserService {

  @Override
  public MinaIntygUser getUser() {
    return (MinaIntygUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
