package se.inera.certificate.web.security;

import com.google.common.collect.ImmutableMap;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.certificate.proxy.filter.HeaderProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 *
 */
public class HttpHeaderProvider implements HeaderProvider {
    @Override
    public Map<String, String> getHeaders(HttpServletRequest httpServletRequest) {
        Citizen citizen = (Citizen) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (citizen != null) {
            return ImmutableMap.of("X-Username", citizen.getUsername());
        }
        return Collections.emptyMap();
    }
}
