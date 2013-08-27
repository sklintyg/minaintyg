package se.inera.certificate.web.security.saml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import se.inera.certificate.web.security.CitizenImpl;
import se.inera.certificate.web.security.LoginMethodEnum;

@Service
public class CitizenAuthenticationService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CitizenAuthenticationService.class);

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        String userid = credential.getNameID().getValue();
        LOG.debug("SAML user: " + userid);
        return new CitizenImpl(userid.substring(0, 8) + "-" + userid.substring(8), LoginMethodEnum.FK);
    }

}