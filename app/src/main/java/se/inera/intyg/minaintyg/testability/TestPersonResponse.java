package se.inera.intyg.minaintyg.testability;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestPersonResponse {

  private List<TestPerson> testPerson;

}
