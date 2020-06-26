/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.jobs;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.infra.integration.ia.jobs.BannerJob;

@EnableScheduling
@Service
public class MIBannerJob extends BannerJob {

    @Override
    protected Application getApplication() {
        return Application.MINA_INTYG;
    }
}
