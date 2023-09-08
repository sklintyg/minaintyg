package se.inera.intyg.minaintyg.auth;

public interface MinaIntygUserDetailService {

  Object getPrincipal(String personId, LoginMethod loginMethod);
}
