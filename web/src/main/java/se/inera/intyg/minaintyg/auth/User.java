package se.inera.intyg.minaintyg.auth;

import java.io.Serializable;
import org.springframework.security.core.userdetails.UserDetails;

public interface User extends UserDetails, Serializable {
    String getPatientId();
}
