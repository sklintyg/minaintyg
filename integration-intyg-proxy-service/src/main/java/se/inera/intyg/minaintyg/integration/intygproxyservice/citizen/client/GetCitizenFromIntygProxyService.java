package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client;


import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;

public interface GetCitizenFromIntygProxyService {

  CitizenResponseDTO getCitizenFromIntygProxy(GetCitizenIntegrationRequest citizenRequest);
}
