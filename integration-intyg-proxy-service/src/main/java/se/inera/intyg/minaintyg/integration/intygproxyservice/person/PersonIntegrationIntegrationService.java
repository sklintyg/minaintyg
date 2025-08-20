package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonIntegrationIntegrationService implements GetPersonIntegrationService {

  private final GetPersonFromIntygProxyService getPersonFromIntygProxyService;
  private final PersonSvarConverter personSvarConverter;

  @Override
  public GetPersonIntegrationResponse getPerson(GetPersonIntegrationRequest personRequest) {
    validateRequest(personRequest);
    final var personSvarDTO = getPersonFromIntygProxyService.getPersonFromIntygProxy(
        personRequest);
    return GetPersonIntegrationResponse.builder()
        .person(personSvarConverter.convertPerson(personSvarDTO.getPerson()))
        .status(personSvarConverter.convertStatus(personSvarDTO.getStatus()))
        .build();
  }

  private void validateRequest(GetPersonIntegrationRequest personRequest) {
    if (personRequest == null || personRequest.getPersonId() == null || personRequest.getPersonId()
        .isEmpty()) {
      throw new IllegalArgumentException("Valid personRequest was not provided: " + personRequest);
    }
  }
}
