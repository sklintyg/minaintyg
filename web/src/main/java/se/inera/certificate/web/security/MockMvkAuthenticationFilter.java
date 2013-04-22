package se.inera.certificate.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.callistasoftware.netcare.mvk.authentication.service.api.impl.AuthenticationResultImpl;
import org.callistasoftware.netcare.mvk.authentication.web.filter.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class MockMvkAuthenticationFilter extends
		AbstractPreAuthenticatedProcessingFilter {

	private static final Logger log = LoggerFactory
			.getLogger(AuthenticationFilter.class);

	//@Value("${mvk.guidParamName}")
	private String guidParameterName = "guid";

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "n/a";
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		log.info("Getting preauthenticated principal");
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth == null) {
			log.debug("Authentication was null, check for guid parameter");
			final String guid = request.getParameter(this.guidParameterName);
			if (guid != null) {
				log.debug(
						"Guid parameter found. Mocking validation against MVK as {}...",
						guid);
				AuthenticationResult mockedResult = AuthenticationResultImpl
						.newPatient(guid);
				return mockedResult;

			}
		} else {
			log.debug("Authentication found. Proceed...");
			return auth.getPrincipal();
		}

		log.warn("Reached end of processing and still no authentication. Return null...");
		return null;
	}

}