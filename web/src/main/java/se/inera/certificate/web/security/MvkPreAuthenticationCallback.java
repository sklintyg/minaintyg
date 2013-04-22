package se.inera.certificate.web.security;

import java.util.Collections;
import java.util.Set;

import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.callistasoftware.netcare.mvk.authentication.service.api.PreAuthenticationCallback;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MvkPreAuthenticationCallback implements PreAuthenticationCallback {

    private Set<SimpleGrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_CITIZEN"));

    @Override
    public UserDetails createMissingUser(AuthenticationResult preAuthenticated) {
        return null;
    }

    @Override
    public UserDetails verifyPrincipal(Object principal) {
        return (UserDetails) principal;
    }

    @Override
    public UserDetails lookupPrincipal(AuthenticationResult auth) throws UsernameNotFoundException {
        System.err.println("Create user from " + auth);
        User user = new User(auth.getUsername(), "1234", roles);
        return user;
    }
}