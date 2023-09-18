package se.inera.intyg.minaintyg.user;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.auth.LoginMethod;

@Value
@Builder
public class UserResponseDTO {

  String personId;
  String personName;
  LoginMethod loginMethod;
}
