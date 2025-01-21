package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

  private PersonId personId;
  private PersonId previousPersonId;
  private String firstName;
  private String lastName;
  private String middleName;
  private String fullName;
  private String street;
  private String city;
  private String zipCode;
  private boolean coordinationNumber;
  private boolean testIndicated;
  private boolean protectedPerson;
  private boolean deceased;
  private boolean differentNameFromEHR;
  private boolean personIdChanged;
  private boolean reserveId;
  private boolean addressFromPU;

}
