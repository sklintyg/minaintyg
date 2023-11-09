package se.inera.intyg.minaintyg.integration.webcert.converter.availablefunction;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.Information;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.AvailableFunctionDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.availablefunction.InformationDTO;

@Component
public class AvailableFunctionConverter {

  public List<AvailableFunction> convert(List<AvailableFunctionDTO> availableFunctions) {
    if (availableFunctions == null) {
      return Collections.emptyList();
    }

    return availableFunctions.stream()
        .map(availableFunctionDTO ->
            AvailableFunction.builder()
                .name(availableFunctionDTO.getName())
                .title(availableFunctionDTO.getTitle())
                .description(availableFunctionDTO.getDescription())
                .type(availableFunctionDTO.getType())
                .body(availableFunctionDTO.getBody())
                .information(convertInformation(availableFunctionDTO.getInformation()))
                .build()
        )
        .toList();
  }

  private List<Information> convertInformation(List<InformationDTO> information) {
    if (information == null) {
      return Collections.emptyList();
    }

    return information.stream()
        .map(informationDTO ->
            Information.builder()
                .id(informationDTO.getId())
                .type(informationDTO.getType())
                .text(informationDTO.getText())
                .build()
        )
        .toList();
  }
}
