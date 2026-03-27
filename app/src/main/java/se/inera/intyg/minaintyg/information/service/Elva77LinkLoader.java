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
package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@Component
@RequiredArgsConstructor
public class Elva77LinkLoader {

  private final ObjectMapper mapper;

  public Elva77MenuConfig load(Resource resource) throws IllegalStateException {
    try (var is = resource.getInputStream()) {
      return mapper.readValue(is, Elva77MenuConfig.class);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load or parse resource: " + resource, e);
    }
  }
}
