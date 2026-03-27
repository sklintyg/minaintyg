/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        .map(
            availableFunctionDTO ->
                AvailableFunction.builder()
                    .name(availableFunctionDTO.getName())
                    .title(availableFunctionDTO.getTitle())
                    .description(availableFunctionDTO.getDescription())
                    .type(availableFunctionDTO.getType())
                    .body(availableFunctionDTO.getBody())
                    .information(convertInformation(availableFunctionDTO.getInformation()))
                    .enabled(availableFunctionDTO.isEnabled())
                    .build())
        .toList();
  }

  private List<Information> convertInformation(List<InformationDTO> information) {
    if (information == null) {
      return Collections.emptyList();
    }

    return information.stream()
        .map(
            informationDTO ->
                Information.builder()
                    .id(informationDTO.getId())
                    .type(informationDTO.getType())
                    .text(informationDTO.getText())
                    .build())
        .toList();
  }
}
