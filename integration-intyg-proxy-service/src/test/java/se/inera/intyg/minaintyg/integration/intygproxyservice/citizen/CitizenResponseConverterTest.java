package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenResponseDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class CitizenResponseConverterTest {

  private static final String PERSON_ID = "personId";
  private static final String FIRSTNAME = "firstname";
  private static final String LASTNAME = "lastNnme";
  private static final boolean IS_ACTIVE = true;

  private final CitizenResponseConverter citizenResponseConverter = new CitizenResponseConverter();

  @Nested
  class ConvertPersonName {

    @Test
    void shouldReturnPersonWithPersonId() {
      final var citizenResponse = getCitizenResponse(null);
      final var result = citizenResponseConverter.convertCitizen(citizenResponse.getCitizen());
      assertEquals(PERSON_ID, result.getPersonId());
    }

    @Test
    void shouldReturnPersonWithIsActiveTrue() {
      final var citizenResponse = getCitizenResponse(null);
      final var result = citizenResponseConverter.convertCitizen(citizenResponse.getCitizen());
      assertEquals(IS_ACTIVE, result.getIsActive());
    }

    @Test
    void shouldReturnPersonWithName() {
      final var citizenResponse = getCitizenResponse(null);
      final var result = citizenResponseConverter.convertCitizen(citizenResponse.getCitizen());
      assertEquals(FIRSTNAME + " " + LASTNAME, result.getName());
    }
  }

  @Nested
  class ConvertStatus {

    @Test
    void shouldReturnCitizenWithStatusFound() {
      final var citizenResponse = getCitizenResponse(StatusDTO.FOUND);
      final var result = citizenResponseConverter.convertStatus(citizenResponse.getStatus());
      assertEquals(Status.FOUND, result);
    }

    @Test
    void shouldReturnCitizenWithStatusNotFound() {
      final var citizenResponse = getCitizenResponse(StatusDTO.NOT_FOUND);
      final var result = citizenResponseConverter.convertStatus(citizenResponse.getStatus());
      assertEquals(Status.NOT_FOUND, result);
    }

    @Test
    void shouldReturnCitizenWithStatusError() {
      final var citizenResponse = getCitizenResponse(StatusDTO.ERROR);
      final var result = citizenResponseConverter.convertStatus(citizenResponse.getStatus());
      assertEquals(Status.ERROR, result);
    }
  }


  private CitizenResponseDTO getCitizenResponse(StatusDTO statusDTO) {
    return CitizenResponseDTO.builder()
        .citizen(
            CitizenDTO.builder()
                .fornamn(FIRSTNAME)
                .efternamn(LASTNAME)
                .personnummer(PERSON_ID)
                .isActive(IS_ACTIVE)
                .build()
        )
        .status(statusDTO)
        .build();
  }

}