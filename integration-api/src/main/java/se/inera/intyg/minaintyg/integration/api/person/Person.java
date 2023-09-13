package se.inera.intyg.minaintyg.integration.api.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

  private String personnummer;
  private String namn;
  private String fornamn;
  private String mellannamn;
  private String efternamn;
}
