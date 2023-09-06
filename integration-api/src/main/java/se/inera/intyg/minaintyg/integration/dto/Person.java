package se.inera.intyg.minaintyg.integration.dto;

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
    private String fornamn;
    private String efternamn;
}
