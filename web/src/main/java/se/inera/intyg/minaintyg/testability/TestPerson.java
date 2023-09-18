package se.inera.intyg.minaintyg.testability;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestPerson {

  String personId;
  String personName;
}
