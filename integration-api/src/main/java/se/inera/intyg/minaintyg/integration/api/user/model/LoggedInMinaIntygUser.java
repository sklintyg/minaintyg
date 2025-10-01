package se.inera.intyg.minaintyg.integration.api.user.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoggedInMinaIntygUser {

  String personId;
}
