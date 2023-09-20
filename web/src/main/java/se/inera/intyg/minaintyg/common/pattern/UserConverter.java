package se.inera.intyg.minaintyg.common.pattern;

import static se.inera.intyg.minaintyg.user.UserToolkit.getUserFromPrincipal;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import se.inera.intyg.minaintyg.util.HashUtility;

public class UserConverter extends ClassicConverter {

  @Override
  public String convert(ILoggingEvent iLoggingEvent) {
    return userInfo();
  }

  private String userInfo() {
    final var minaIntygUser = getUserFromPrincipal();
    if (minaIntygUser.isEmpty()) {
      return "noUser";
    }
    final var user = minaIntygUser.get();
    return HashUtility.hash(user.getPersonId()) + "," + user.getLoginMethod();
  }
}
