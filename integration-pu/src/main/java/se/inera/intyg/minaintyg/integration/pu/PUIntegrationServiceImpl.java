package se.inera.intyg.minaintyg.integration.pu;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.PUIntegrationService;
import se.inera.intyg.minaintyg.integration.dto.PersonResponse;
import se.inera.intyg.minaintyg.integration.dto.Person;
import se.inera.intyg.minaintyg.integration.dto.Status;

@Component
public class PUIntegrationServiceImpl implements PUIntegrationService {

    @Override
    public PersonResponse get(String personId) {
        return PersonResponse.builder()
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
