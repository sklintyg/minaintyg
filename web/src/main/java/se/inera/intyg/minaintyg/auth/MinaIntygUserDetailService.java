package se.inera.intyg.minaintyg.auth;

public interface MinaIntygUserDetailService {

  MinaIntygUser buildPrincipal(String personId, LoginMethod loginMethod);
}
