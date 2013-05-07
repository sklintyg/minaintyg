package se.inera.certificate.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 */
public class ClearSecurityContextFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ClearSecurityContextFilter.class);

    private LogoutHandler handler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        log.debug("Clearing security context");
        handler.logout((HttpServletRequest) request, (HttpServletResponse) response, null);
        filterChain.doFilter(request, response);

    }

    public void setHandler(LogoutHandler handler) {
        this.handler = handler;
    }

}
