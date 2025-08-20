package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationService;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.GetCitizenFromIntygProxyService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitizenIntegrationIntegrationService implements GetCitizenIntegrationService {

  private final GetCitizenFromIntygProxyService getCitizenFromIntygProxyService;
  private final CitizenResponseConverter citizenResponseConverter;

  @Override
  public GetCitizenIntegrationResponse getCitizen(GetCitizenIntegrationRequest citizenRequest) {
    validateRequest(citizenRequest);
    final var citizenResponseDTO = getCitizenFromIntygProxyService.getCitizenFromIntygProxy(
        citizenRequest);
    return GetCitizenIntegrationResponse.builder()
        .citizen(citizenResponseConverter.convertCitizen(citizenResponseDTO.getCitizen()))
        .status(citizenResponseConverter.convertStatus(citizenResponseDTO.getStatus()))
        .build();
  }

  private void validateRequest(GetCitizenIntegrationRequest citizenRequest) {
    if (citizenRequest == null || citizenRequest.getCitizenId() == null || citizenRequest.getCitizenId()
        .isEmpty()) {
      throw new IllegalArgumentException("Valid citizenRequest was not provided: " + citizenRequest);
    }
  }
}
