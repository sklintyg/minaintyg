package se.inera.intyg.minaintyg.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonService;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.Status;
import se.inera.intyg.minaintyg.user.MinaIntygUserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaIntygUserDetailServiceImpl implements MinaIntygUserDetailService {

  private final GetPersonService getPersonService;
  private final MinaIntygUserService minaIntygUserService;

  @Override
  public MinaIntygUser buildPrincipal(String personId, LoginMethod loginMethod) {
    validatePersonId(personId);
    final var personResponse = getPersonService.getPerson(
        PersonRequest.builder()
            .personId(personId)
            .build()
    );
    if (!personResponse.getStatus().equals(Status.FOUND)) {
      handleCommunicationFault(personResponse.getStatus());
    }
    return minaIntygUserService.buildUserFromPersonResponse(personResponse, loginMethod);
  }

  private static void handleCommunicationFault(Status status) {
    log.error("Error communicating with IntygProxyService, status from response: '{}",
        status);
    throw new RuntimeException("Error communication with IntygProxyService. Status: " + status);
  }

  private void validatePersonId(String personId) {
    if (personId == null || personId.trim().isEmpty()) {
      throw new IllegalArgumentException(
          String.format("personId must have a valid value: '%s'", personId));
    }
  }
}
