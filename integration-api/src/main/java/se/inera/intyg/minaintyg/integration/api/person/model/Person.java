package se.inera.intyg.minaintyg.integration.api.person.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Person {

  String personId;
  String name;
}
