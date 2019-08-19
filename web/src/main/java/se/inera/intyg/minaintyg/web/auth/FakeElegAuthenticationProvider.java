/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.minaintyg.web.auth.CgiElegAssertion.FAKE_AUTHENTICATION_ELEG_CONTEXT_REF;

import java.util.ArrayList;
import java.util.Collection;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.security.saml.SAMLCredential;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.minaintyg.web.integration.pu.MinaIntygPUService;
import se.inera.intyg.minaintyg.web.integration.pu.PersonNameUtil;
import se.inera.intyg.minaintyg.web.security.CitizenImpl;
import se.inera.intyg.minaintyg.web.security.LoginMethodEnum;

/**
 * Fake authentication provider for E-leg.
 *
 * Only supports {@link FakeElegAuthenticationToken}, e.g. must be unusable for real authentications.
 */
public class FakeElegAuthenticationProvider extends BaseFakeAuthenticationProvider {

    private MinaIntygUserDetailsService minaIntygUserDetailsService;

    private MinaIntygPUService minaIntygPUService;

    private PersonNameUtil personNameUtil = new PersonNameUtil();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SAMLCredential credential = createSamlCredential(authentication);
        Object details = minaIntygUserDetailsService.loadUserBySAML(credential);

        // Set origin (FK or ELVA77) from fake credentials.
        if (authentication instanceof FakeElegAuthenticationToken && details instanceof CitizenImpl) {
            FakeElegCredentials credz = (FakeElegCredentials) authentication
                .getCredentials();
            Person person = minaIntygPUService.getPerson(credz.getPersonId());
            details = new CitizenImpl(credz.getPersonId(), LoginMethodEnum.fromValue(credz.getOrigin()),
                personNameUtil.buildFullName(person), person.isSekretessmarkering());
        }

        ExpiringUsernameAuthenticationToken result = new ExpiringUsernameAuthenticationToken(null, details, credential,
            buildGrantedAuthorities(details));
        result.setDetails(details);

        return result;
    }

    private Collection<? extends GrantedAuthority> buildGrantedAuthorities(Object details) {

        if (details instanceof CitizenImpl) {
            return ((CitizenImpl) details).getAuthorities();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FakeElegAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private SAMLCredential createSamlCredential(Authentication token) {
        FakeElegCredentials fakeCredentials = (FakeElegCredentials) token
            .getCredentials();

        Assertion assertion = new AssertionBuilder().buildObject();

        attachAuthenticationContext(assertion, FAKE_AUTHENTICATION_ELEG_CONTEXT_REF);

        AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();
        assertion.getAttributeStatements().add(attributeStatement);

        attributeStatement.getAttributes().add(createAttribute(CgiElegAssertion.PERSON_ID_ATTRIBUTE, fakeCredentials.getPersonId()));
        attributeStatement.getAttributes().add(createAttribute(CgiElegAssertion.FORNAMN_ATTRIBUTE, fakeCredentials.getFirstName()));
        attributeStatement.getAttributes().add(
            createAttribute(CgiElegAssertion.MELLAN_OCH_EFTERNAMN_ATTRIBUTE, fakeCredentials.getLastName()));

        NameID nameId = new NameIDBuilder().buildObject();
        nameId.setValue(token.getCredentials().toString());
        return new SAMLCredential(nameId, assertion, "fake-idp", "minaintyg");
    }

    @Autowired
    public void setMinaIntygUserDetailsService(MinaIntygUserDetailsService minaIntygUserDetailsService) {
        this.minaIntygUserDetailsService = minaIntygUserDetailsService;
    }

    @Autowired
    public void setMinaIntygPUService(MinaIntygPUService minaIntygPUService) {
        this.minaIntygPUService = minaIntygPUService;
    }
}
