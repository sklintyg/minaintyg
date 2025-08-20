package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String personnummer;
  private String namn;
  private String fornamn;
  private String mellannamn;
  private String efternamn;
}
