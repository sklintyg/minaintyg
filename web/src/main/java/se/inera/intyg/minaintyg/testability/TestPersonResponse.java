package se.inera.intyg.minaintyg.testability;

import java.util.List;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class TestPersonResponse {

    private List<TestPerson> testPerson;

}
