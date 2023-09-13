package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonService;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonIntegrationService implements GetPersonService {

  private final GetPersonFromIntygProxyService getPersonFromIntygProxyService;
  private final PersonConverterService personConverterService;

  @Override
  public PersonResponse getPerson(PersonRequest personRequest) {
    validateRequest(personRequest);
    try {
      final var personFromIntygProxy = getPersonFromIntygProxyService.getPersonFromIntygProxy(
          personRequest);
      return PersonResponse.builder()
          .person(personConverterService.convert(personFromIntygProxy.getPerson()))
          .status(personFromIntygProxy.getStatus())
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private void validateRequest(PersonRequest personRequest) {
    if (personRequest == null || personRequest.getPersonId() == null || personRequest.getPersonId()
        .isEmpty()) {
      throw new IllegalArgumentException("Valid personRequest was not provided: " + personRequest);
    }
  }
}
