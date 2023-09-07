package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonService;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.api.person.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyService;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyServiceImpl;

@Slf4j
@Service
public class PersonIntegrationService implements GetPersonService {

  private final GetPersonFromIntygProxyService getPersonFromIntygProxyService;

  public PersonIntegrationService(
      GetPersonFromIntygProxyServiceImpl getPersonFromIntygProxyService) {
    this.getPersonFromIntygProxyService = getPersonFromIntygProxyService;
  }

  @Override
  public PersonResponse getPerson(PersonRequest personRequest) {
    validateRequest(personRequest);
    try {
      return getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest);
    } catch (Exception exception) {
      return PersonResponse.builder()
          .status(Status.ERROR)
          .build();
    }
  }

  private void validateRequest(PersonRequest personRequest) {
    if (personRequest == null || personRequest.getPersonId() == null || personRequest.getPersonId()
        .isEmpty()) {
      log.error("No valid personRequest was provided: {}, cannot get person from intygproxy.",
          personRequest);
      throw new IllegalArgumentException("Valid personRequest was not provided: " + personRequest);
    }
  }
}
