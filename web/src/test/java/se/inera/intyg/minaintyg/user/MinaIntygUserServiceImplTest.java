package se.inera.intyg.minaintyg.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.testhelper.TestPrincipalHelper;

class MinaIntygUserServiceImplTest {

  private static final String PERSON_ID = "personId";
  private static final String PERSON_NAME = "personName";
  private static final String FIRSTNAME = "firstname";
  private static final String LASTNAME = "lastNnme";
  private static final String SURNAME = "surname";
  private static final LoginMethod LOGIN_METHOD = LoginMethod.ELVA77;

  private final MinaIntygUserServiceImpl minaIntygUserService = new MinaIntygUserServiceImpl();

  @Nested
  class GetUser {

    @Test
    void shouldReturnUserFromPrincipal() {
      final var expectedUser = Optional.of(
          MinaIntygUser.builder()
              .personId(PERSON_ID)
              .personName(PERSON_NAME)
              .loginMethod(LOGIN_METHOD)
              .build()
      );
      TestPrincipalHelper.setMinaIntygUserAsPrincipal(expectedUser.get());
      final var actualUser = minaIntygUserService.getUser();
      assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldNotReturnUserFromPrincipalIfPrincipalNotIsInstanceOfMinaIntygUser() {
      final var user = new Object();
      TestPrincipalHelper.setUnknownPrincipal(user);
      final var actualUser = minaIntygUserService.getUser();
      assertTrue(actualUser.isEmpty());
    }
  }

  @Nested
  class BuildUser {

    @Test
    void shouldBuildUserWithPersonIdFromPersonResponse() {
      final var personResponse = getPersonResponse(null);
      final var result = minaIntygUserService.buildUserFromPersonResponse(personResponse,
          LOGIN_METHOD);
      assertEquals(PERSON_ID, result.getPersonId());
    }

    @Test
    void shouldBuildUserWithPersonNameFromPersonResponse() {
      final var personResponse = getPersonResponse(null);
      final var result = minaIntygUserService.buildUserFromPersonResponse(personResponse,
          LOGIN_METHOD);
      assertEquals(FIRSTNAME + " " + LASTNAME, result.getPersonName());
    }

    @Test
    void shouldBuildUserWithPersonNameIncludedSurnameFromPersonResponse() {
      final var personResponse = getPersonResponse(SURNAME);
      final var result = minaIntygUserService.buildUserFromPersonResponse(personResponse,
          LOGIN_METHOD);
      assertEquals(FIRSTNAME + " " + SURNAME + " " + LASTNAME, result.getPersonName());
    }

    @Test
    void shouldBuildUserWithLoginMethodFromPersonResponse() {
      final var personResponse = getPersonResponse(SURNAME);
      final var result = minaIntygUserService.buildUserFromPersonResponse(personResponse,
          LOGIN_METHOD);
      assertEquals(LOGIN_METHOD, result.getLoginMethod());
    }


    private PersonResponse getPersonResponse(String surname) {
      return PersonResponse.builder()
          .person(
              Person.builder()
                  .fornamn(FIRSTNAME)
                  .mellannamn(surname)
                  .efternamn(LASTNAME)
                  .personnummer(PERSON_ID)
                  .build()
          )
          .build();
    }
  }
}
