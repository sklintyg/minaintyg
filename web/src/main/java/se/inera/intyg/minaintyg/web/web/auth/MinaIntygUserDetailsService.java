package se.inera.intyg.minaintyg.web.web.auth;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import se.inera.intyg.common.support.common.util.HashUtility;
import se.inera.intyg.minaintyg.web.web.security.CitizenImpl;
import se.inera.intyg.minaintyg.web.web.security.LoginMethodEnum;

/**
 * Created by eriklupander on 2017-03-09.
 */
public class MinaIntygUserDetailsService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(MinaIntygUserDetailsService.class);

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        String personId = getAttribute(credential, CgiElegAssertion.PERSON_ID_ATTRIBUTE);
        String firstName = getAttribute(credential, CgiElegAssertion.FORNAMN_ATTRIBUTE);
        String lastName = getAttribute(credential, CgiElegAssertion.MELLAN_OCH_EFTERNAMN_ATTRIBUTE);

        LOG.info("Got " + HashUtility.hash(personId) + " with name: " + firstName + " " + lastName);

        return new CitizenImpl(personId, LoginMethodEnum.ELVA77);
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
}
