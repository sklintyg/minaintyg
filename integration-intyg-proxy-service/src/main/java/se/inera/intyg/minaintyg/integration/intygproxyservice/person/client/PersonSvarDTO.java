package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonSvarDTO {

  private PersonDTO person;
  private StatusDTO status;
}
