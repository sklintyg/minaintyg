package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationService;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetUserFromIntygProxyService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIntegrationIntegrationService implements GetUserIntegrationService {

  private final GetUserFromIntygProxyService getUserFromIntygProxyService;
  private final UserResponseConverter userResponseConverter;

  @Override
  public GetUserIntegrationResponse getUser(GetUserIntegrationRequest personRequest) {
    validateRequest(personRequest);
    final var userResponseDTO = getUserFromIntygProxyService.getUserFromIntygProxy(
        personRequest);
    return GetUserIntegrationResponse.builder()
        .user(userResponseConverter.convertUser(userResponseDTO.getPerson()))
        .status(userResponseConverter.convertStatus(userResponseDTO.getStatus()))
        .build();
  }

  private void validateRequest(GetUserIntegrationRequest personRequest) {
    if (personRequest == null || personRequest.getUserId() == null || personRequest.getUserId()
        .isEmpty()) {
      throw new IllegalArgumentException("Valid personRequest was not provided: " + personRequest);
    }
  }
}
