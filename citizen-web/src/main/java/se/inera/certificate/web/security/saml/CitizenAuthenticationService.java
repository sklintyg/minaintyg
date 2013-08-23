package se.inera.certificate.web.security.saml;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import se.inera.certificate.web.security.CitizenImpl;
import se.inera.certificate.web.security.LoginMethodEnum;

@Service
public class CitizenAuthenticationService implements SAMLUserDetailsService {

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        String userid = credential.getNameID().getValue();
        return new CitizenImpl(userid.substring(0, 8) + "-" + userid.substring(8), LoginMethodEnum.FK);
    }

}