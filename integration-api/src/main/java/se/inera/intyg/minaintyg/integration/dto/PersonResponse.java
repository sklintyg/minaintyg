package se.inera.intyg.minaintyg.integration.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponse {

    private Person person;
    private Status status;
}
