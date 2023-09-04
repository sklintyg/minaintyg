package se.inera.intyg.minaintyg.filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import se.inera.intyg.minaintyg.auth.FakeAuthenticationToken;
import se.inera.intyg.minaintyg.auth.FakeCredentials;

public class FakeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FakeAuthenticationFilter.class);

    public FakeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/fake/sso", HttpMethod.POST.name()));
        LOG.error("FakeAuthentication enabled. DO NOT USE IN PRODUCTION");
    }

    private Authentication performFakeElegAuthentication(String personId) {
        final var fakeCredentials = new FakeCredentials();
        fakeCredentials.setPersonId(personId);
        fakeCredentials.setPersonName("test");
        LOG.info("Detected fake credentials " + fakeCredentials);
        return getAuthenticationManager().authenticate(new FakeAuthenticationToken(fakeCredentials));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        final var personId = request.getParameter("personId");
        final var json = URLDecoder.decode(personId, StandardCharsets.UTF_8);
        return performFakeElegAuthentication(json);
    }
}
