package se.inera.intyg.minaintyg.auth;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

public class MinaIntygUserDetailsService implements SAMLUserDetailsService {

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        String personId = getAttribute(credential);
        return new MinaIntygUserImpl(personId);
    }

    private String getAttribute(SAMLCredential samlCredential) {
        for (AttributeStatement attributeStatement : samlCredential.getAuthenticationAssertion().getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                if (attribute.getName().equals(AuthenticationConstants.PERSON_ID_ATTRIBUTE)) {
                    if (!attribute.getAttributeValues().isEmpty()) {
                        XMLObject xmlObject = attribute.getAttributeValues().get(0);
                        if (xmlObject instanceof XSString && ((XSString) xmlObject).getValue() != null) {
                            return ((XSString) xmlObject).getValue();
                        } else if (xmlObject.getDOM() != null) {
                            return xmlObject.getDOM().getTextContent();
                        }
                        throw new IllegalArgumentException(
                            "Cannot parse SAML2 response attribute '" + AuthenticationConstants.PERSON_ID_ATTRIBUTE + "', is not XSString or DOM is null");
                    }
                }
            }
        }
        throw new IllegalArgumentException("Could not extract attribute '" + AuthenticationConstants.PERSON_ID_ATTRIBUTE + "' from SAMLCredential.");
    }
}
