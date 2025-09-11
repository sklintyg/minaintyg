package se.inera.intyg.minaintyg.information.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DynamicLink {

  String id;
  String name;
  Map<String, String> urls;
}
