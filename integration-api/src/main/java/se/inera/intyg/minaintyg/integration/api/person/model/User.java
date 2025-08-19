package se.inera.intyg.minaintyg.integration.api.person.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {

  String userId;
  String name;
  boolean isActive;
}
