/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.registry.ModuleNotFoundException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.Certificate;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.web.util.CertificateMetaConverter;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * Controller that exposes a REST interface to functions common to certificate modules, such as get and send
 * certificate.
 *
 * @author marced
 */
public class ModuleApiController {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiController.class);
    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    //Relevant statuses for this context
    private static final List<CertificateState> RELEVANT_STATUS_TYPES = Arrays.asList(CertificateState.SENT, CertificateState.CANCELLED);


    @Autowired
    private IntygModuleRegistry moduleRegistry;

    /**
     * Helper service to get current user.
     */
    @Autowired
    private CitizenService citizenService;

    /**
     * Intygstjanstens WS endpoint service.
     */
    @Autowired
    private CertificateService certificateService;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    /**
     * Return the certificate identified by the given id as JSON.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in JSON format
     */
    @GET
    @Path("/{type}/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final Response getCertificate(@PathParam("type") final String type, @PathParam("id") final String id) {
        LOG.debug("getCertificate: {}", id);

        Optional<CertificateResponse> utlatande = certificateService.getUtlatande(type,
                new Personnummer(citizenService.getCitizen().getUsername()),
                id);
        if (utlatande.isPresent()) {
            try {
                JsonNode utlatandeJson = objectMapper.readTree(utlatande.get().getInternalModel());
                CertificateMeta meta = CertificateMetaConverter.toCertificateMeta(utlatande.get().getMetaData(), RELEVANT_STATUS_TYPES);
                return Response.ok(new Certificate(utlatandeJson, meta)).build();

            } catch (IOException e) {
                LOG.error("Failed to serialize module-internal representation of certificate " + id);
                return Response.serverError().build();
            }
        } else {
            return Response.serverError().build();
        }
    }


    /**
     * Return the certificate identified by the given id as PDF.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in PDF format
     */
    @GET
    @Path("/{type}/{id}/pdf")
    @Produces("application/pdf")
    public final Response getCertificatePdf(@PathParam(value = "type") final String type, @PathParam(value = "id") final String id) {
        LOG.debug("getCertificatePdf: {}", id);
        return getPdfInternal(type, id, null, false);
    }

    /**
     * Return the certificate identified by the given id as customized employer copy PDF rendition.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in PDF format
     */
    @POST
    @Path("/{type}/{id}/pdf/arbetsgivarutskrift")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/pdf")
    public final Response getCertificatePdfEmployerCopy(@PathParam(value = "type") final String type,
            @PathParam(value = "id") final String id,
            @FormParam("selectedOptionalFields") List<String> selectedOptionalFields) {
        LOG.debug("getCertificatePdfEmployerCopy: id {}, selectedOptionalFields {}", id, selectedOptionalFields);
        return getPdfInternal(type, id, selectedOptionalFields, true);

    }

    private Response getPdfInternal(String type, String id, List<String> optionalFields, boolean isEmployerCopy) {
        Optional<CertificateResponse> utlatande = certificateService.getUtlatande(type,
                new Personnummer(citizenService.getCitizen().getUsername()), id);
        if (utlatande.isPresent()) {
            String typ = utlatande.get().getUtlatande().getTyp();
            try {
                ModuleApi moduleApi = moduleRegistry.getModuleApi(typ);
                List<Status> statusList = utlatande.get().getMetaData().getStatus().stream()
                        .filter(s -> CertificateState.SENT.equals(s.getType()))
                        .collect(Collectors.toList());

                PdfResponse pdf;

                if (isEmployerCopy) {
                    pdf = moduleApi.pdfEmployer(utlatande.get().getInternalModel(), statusList, ApplicationOrigin.MINA_INTYG,
                            optionalFields);
                } else {
                    pdf = moduleApi.pdf(utlatande.get().getInternalModel(), statusList, ApplicationOrigin.MINA_INTYG);
                }

                return Response.ok(pdf.getPdfData())
                        .header(CONTENT_DISPOSITION, "attachment; filename=" + pdf.getFilename())
                        .build();

            } catch (ModuleNotFoundException e) {
                LOG.error("Module " + typ + " not found. Not loaded in application.");
                return Response.serverError().build();
            } catch (ModuleException e) {
                LOG.error("Failed to get PDF for certificate " + id + " from inera-certificate.");
                return Response.serverError().build();
            }
        } else {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
