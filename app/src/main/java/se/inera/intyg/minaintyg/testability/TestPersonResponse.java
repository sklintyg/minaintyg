package se.inera.intyg.minaintyg.testability;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestPersonResponse {

  List<TestPerson> testPerson;

}
