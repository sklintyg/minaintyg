package se.inera.intyg.minaintyg.information.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.information.service.model.Elva77Menu.Elva77MenuBuilder;

@Value
@Builder
@JsonDeserialize(builder = Elva77MenuBuilder.class)
public class Elva77Menu {

  List<Elva77MenuLinks> items;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Elva77MenuBuilder {

  }
}
