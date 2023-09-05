package se.inera.intyg.minaintyg.integration.pu;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.PUIntegrationService;
import se.inera.intyg.minaintyg.integration.dto.PUResponse;
import se.inera.intyg.minaintyg.integration.dto.Person;
import se.inera.intyg.minaintyg.integration.dto.Status;

@Component
public class PUIntegrationServiceImpl implements PUIntegrationService {

    @Override
    public PUResponse get(String personId) {
        return PUResponse.builder()
            .person(
                Person.builder()
                    .personId("personId")
                    .name("name")
                    .build()
            )
            .status(Status.FOUND)
            .build();
    }
}
