package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyService;

@Service
public class PersonIntegrationServiceImpl implements GetPersonFromIntygProxyService {

    private final GetPersonFromIntygProxyService getPersonFromIntygProxyService;

    public PersonIntegrationServiceImpl(GetPersonFromIntygProxyService getPersonFromIntygProxyService) {
        this.getPersonFromIntygProxyService = getPersonFromIntygProxyService;
    }

    @Override
    public PersonResponse getPerson(PersonRequest personRequest) {
        return getPersonFromIntygProxyService.getPerson(personRequest);
    }
}
