package se.inera.intyg.minaintyg.filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import se.inera.intyg.minaintyg.auth.FakeAuthenticationToken;
import se.inera.intyg.minaintyg.auth.FakeCredentials;

@Slf4j
public class FakeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public FakeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/fake/sso", HttpMethod.POST.name()));
        log.error("FakeAuthentication enabled. DO NOT USE IN PRODUCTION");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        final var personId = request.getParameter("personId");
        return performFakeElegAuthentication(personId);
    }

    private Authentication performFakeElegAuthentication(String personId) {
        final var fakeCredentials = new FakeCredentials();
        fakeCredentials.setPersonId(personId);
        log.info("Detected fake credentials " + fakeCredentials);
        return getAuthenticationManager().authenticate(new FakeAuthenticationToken(fakeCredentials, null, null));
    }

}
