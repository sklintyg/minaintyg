package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.person.model.User;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.UserDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@Component
public class UserResponseConverter {

  private static final String SPACE = " ";
  private static final String EMPTY = "";
  private static final Map<StatusDTO, Status> STATUS_MAP = Map.of(
      StatusDTO.FOUND, Status.FOUND,
      StatusDTO.NOT_FOUND, Status.NOT_FOUND,
      StatusDTO.ERROR, Status.ERROR
  );

  public User convertUser(UserDTO userDTO) {
    return User.builder()
        .userId(userDTO.getPersonnummer())
        .name(buildUserName(userDTO))
        .isActive(userDTO.isActive())
        .build();
  }

  public Status convertStatus(StatusDTO statusDTO) {
    return STATUS_MAP.get(statusDTO);
  }

  private String buildUserName(UserDTO userDTO) {
    return userDTO.getFornamn()
        + SPACE
        + includeMiddleName(userDTO.getMellannamn())
        + userDTO.getEfternamn();
  }

  private String includeMiddleName(String middleName) {
    return middleName != null ? middleName + SPACE : EMPTY;
  }
}
