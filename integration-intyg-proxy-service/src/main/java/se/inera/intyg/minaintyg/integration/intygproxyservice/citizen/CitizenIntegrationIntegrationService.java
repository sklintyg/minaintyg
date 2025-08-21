package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import static se.inera.intyg.minaintyg.integration.api.citizen.CitizenConstants.CITIZEN_IPS_INTEGRATION;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.PersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.GetCitizenFromIntygProxyService;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile(CITIZEN_IPS_INTEGRATION)
public class CitizenIntegrationIntegrationService implements GetPersonIntegrationService {

  private final GetCitizenFromIntygProxyService getCitizenFromIntygProxyService;
  private final CitizenResponseConverter citizenResponseConverter;

  @Override
  public PersonIntegrationResponse getPerson(GetPersonIntegrationRequest personRequest) {
    validateRequest(personRequest);
    final var citizenResponseDTO = getCitizenFromIntygProxyService.getCitizenFromIntygProxy(
        GetCitizenIntegrationRequest.builder()
            .personId(personRequest.getPersonId())
            .build()
    );
    return GetCitizenIntegrationResponse.builder()
        .citizen(citizenResponseConverter.convertCitizen(citizenResponseDTO.getCitizen()))
        .status(citizenResponseConverter.convertStatus(citizenResponseDTO.getStatus()))
        .build();
  }

  private void validateRequest(GetPersonIntegrationRequest citizenRequest) {
    if (citizenRequest == null || citizenRequest.getPersonId() == null
        || citizenRequest.getPersonId()
        .isEmpty()) {
      throw new IllegalArgumentException(
          "Valid citizenRequest was not provided: " + citizenRequest);
    }
  }
}