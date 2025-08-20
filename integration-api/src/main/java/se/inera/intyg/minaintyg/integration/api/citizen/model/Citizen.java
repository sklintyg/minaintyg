package se.inera.intyg.minaintyg.integration.api.citizen.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Citizen {

  String citizenId;
  String name;
  boolean isActive;
}
