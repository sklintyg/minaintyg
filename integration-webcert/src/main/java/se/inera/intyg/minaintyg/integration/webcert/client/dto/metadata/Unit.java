package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit.UnitBuilder;

@JsonDeserialize(builder = UnitBuilder.class)
@Value
@Builder
public class Unit {

  private String unitId;
  private String unitName;
  private String address;
  private String zipCode;
  private String city;
  private String phoneNumber;
  private String email;
  private Boolean isInactive;

  @JsonPOJOBuilder(withPrefix = "")
  public static class UnitBuilder {

  }
}
