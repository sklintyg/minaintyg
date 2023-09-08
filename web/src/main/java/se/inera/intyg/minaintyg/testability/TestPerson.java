package se.inera.intyg.minaintyg.testability;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestPerson {

  private String personId;
  private String personName;
}
