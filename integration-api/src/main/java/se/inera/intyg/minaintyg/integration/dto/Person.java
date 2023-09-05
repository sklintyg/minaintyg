package se.inera.intyg.minaintyg.integration.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

    private String personId;
    private String name;
}
