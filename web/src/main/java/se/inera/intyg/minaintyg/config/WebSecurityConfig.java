package se.inera.intyg.minaintyg.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.DEV_PROFILE;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import se.inera.intyg.minaintyg.auth.MinaIntygLoggingSessionRegistryImpl;
import se.inera.intyg.minaintyg.auth.MinaIntygUserDetailsService;
import se.inera.intyg.minaintyg.service.monitoring.MonitoringLogService;

@Profile("" + DEV_PROFILE)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final MonitoringLogService monitoringLogService;
    private final MinaIntygUserDetailsService minaIntygUserDetailsService;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    @Bean
    public Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }


    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionAuthenticationStrategy());
    }
    @Bean
    public MinaIntygLoggingSessionRegistryImpl<? extends Session> sessionAuthenticationStrategy() {
        return new MinaIntygLoggingSessionRegistryImpl<>(sessionRepository, monitoringLogService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RelyingPartyRegistrationRepository relyingPartyRegistrationRepository) throws Exception {
        final var relyingPartyRegistrationResolver = new DefaultRelyingPartyRegistrationResolver(relyingPartyRegistrationRepository);
        final var metadataFilter = new Saml2MetadataFilter(relyingPartyRegistrationResolver, new OpenSamlMetadataResolver());

        http.httpBasic(filterSecurity -> filterSecurity.authenticationEntryPoint(loginAuthenticationEntryPoint()))
            .authorizeHttpRequests(WebSecurityConfig::configureAlwaysPermitted)
            .authorizeHttpRequests(request -> request.requestMatchers("/**").fullyAuthenticated())
            .authorizeHttpRequests(request -> request.anyRequest().authenticated())
            .exceptionHandling((exceptionHandlingConfigurer) -> {
                    exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(
                        loginAuthenticationEntryPoint(),
                        new AntPathRequestMatcher("/saml/**")
                    );
                    exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(
                        http403ForbiddenEntryPoint(),
                        AnyRequestMatcher.INSTANCE);
                }
            )
            .saml2Login(withDefaults())
            .saml2Logout(withDefaults())
            .addFilterBefore(metadataFilter, Saml2WebSsoAuthenticationFilter.class)
            .userDetailsService(minaIntygUserDetailsService)
            .sessionManagement(securitySessionManagementConfigurer -> securitySessionManagementConfigurer.sessionAuthenticationStrategy(registerSessionAuthenticationStrategy()))
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults());

        return http.build();
    }


    private static void configureAlwaysPermitted(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        request.requestMatchers("/").permitAll();
        request.requestMatchers("/ping").permitAll();
        request.requestMatchers("/links").permitAll();
        request.requestMatchers("/config").permitAll();
    }

    @Bean
    public AuthenticationEntryPoint loginAuthenticationEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/");
    }

    // https://stackoverflow.com/questions/72508155/spring-saml2-and-spring-session-savedrequest-not-retrieved-cannot-redirect-to
    @Bean
    public DefaultCookieSerializerCustomizer cookieSerializerCustomizer() {
        return cookieSerializer -> {
            cookieSerializer.setSameSite(null);
        };
    }
}
