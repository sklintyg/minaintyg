package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinaIntygUser implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  Set<SimpleGrantedAuthority> roles = Collections.singleton(
      new SimpleGrantedAuthority("ROLE_ORGANIZATION_DELEGATE"));
  private String personId;
  private String personName;
  private LoginMethod loginMethod;

}
