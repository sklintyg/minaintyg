/**
 * Copyright (C) 2012 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.controller.moduleapi;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.inera.certificate.integration.IneraCertificateRestApi;

/**
 * Controller that exposes a REST interface to functions common to certificate modules, such as get and send certificate.
 * 
 * @author marced
 * 
 */
@Controller
@RequestMapping(value = "/certificate")
public class ModuleApiController {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiController.class);

    /**
     * Intygstjanstens REST endpoint service.
     */
    @Autowired
    private IneraCertificateRestApi certificateRestService;

    /**
     * Return the certificate identified by the given id.
     * 
     * @param id - the globally unique id of a certificate.
     * @return The certificate in JSON format
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ResponseBody
    public final String getCertificate(@PathVariable(value = "id") final String id) {
        LOG.debug("getCertificate: {}", id);
        return certificateRestService.getCertificate(id);
    }
}
