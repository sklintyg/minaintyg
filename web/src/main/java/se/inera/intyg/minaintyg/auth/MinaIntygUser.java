package se.inera.intyg.minaintyg.auth;

import java.io.Serializable;
import org.springframework.security.core.userdetails.UserDetails;

public interface MinaIntygUser extends UserDetails, Serializable {

    String getPersonId();
}
