package se.inera.intyg.minaintyg.monitoring;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.Objects;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.util.HashUtility;

public class UserConverter extends ClassicConverter {

  @Override
  public String convert(ILoggingEvent iLoggingEvent) {
    return userInfo();
  }

  private String userInfo() {
    final var minaIntygUser = minaIntygUser();
    if (minaIntygUser == null) {
      return "noUser";
    }
    return HashUtility.hash(minaIntygUser.getPersonId()) + "," + minaIntygUser.getLoginMethod();
  }

  private MinaIntygUser minaIntygUser() {
    final var auth = SecurityContextHolder.getContext().getAuthentication();

    if (Objects.isNull(auth)) {
      return null;
    }

    final var principal = auth.getPrincipal();

    return (principal instanceof MinaIntygUser) ? (MinaIntygUser) principal : null;
  }

}
