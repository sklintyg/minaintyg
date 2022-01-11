/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.auth;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.minaintyg.web.integration.pu.MinaIntygPUService;
import se.inera.intyg.minaintyg.web.integration.pu.PersonNameUtil;
import se.inera.intyg.minaintyg.web.security.CitizenImpl;
import se.inera.intyg.minaintyg.web.security.LoginMethodEnum;

/**
 * Created by eriklupander on 2017-03-09.
 */
public class MinaIntygUserDetailsService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(MinaIntygUserDetailsService.class);

    private MinaIntygPUService minaIntygPUService;

    private PersonNameUtil personNameUtil = new PersonNameUtil();

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        String personId = getAttribute(credential, CgiElegAssertion.PERSON_ID_ATTRIBUTE);
        Person person = minaIntygPUService.getPerson(personId);
        String personName = personNameUtil.buildFullName(person);

        LOG.info("Got {} with name: {}", person.getPersonnummer().getPersonnummerHash(), personName);

        return new CitizenImpl(personId, LoginMethodEnum.ELVA77, personName, person.isSekretessmarkering());
    }

    private String getAttribute(SAMLCredential samlCredential, String attributeName) {
        for (AttributeStatement attributeStatement : samlCredential.getAuthenticationAssertion().getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                if (attribute.getName().equals(attributeName)) {

                    if (!attribute.getAttributeValues().isEmpty()) {
                        XMLObject xmlObject = attribute.getAttributeValues().get(0);
                        if (xmlObject instanceof XSString && ((XSString) xmlObject).getValue() != null) {
                            return ((XSString) xmlObject).getValue();
                        } else if (xmlObject.getDOM() != null) {
                            return xmlObject.getDOM().getTextContent();
                        }
                        throw new IllegalArgumentException(
                            "Cannot parse SAML2 response attribute '" + attributeName + "', is not XSString or DOM is null");
                    }
                }
            }
        }
        throw new IllegalArgumentException("Could not extract attribute '" + attributeName + "' from SAMLCredential.");
    }

    public void setMinaIntygPUService(MinaIntygPUService minaIntygPUService) {
        this.minaIntygPUService = minaIntygPUService;
    }
}
