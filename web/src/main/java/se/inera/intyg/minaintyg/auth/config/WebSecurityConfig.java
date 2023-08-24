package se.inera.intyg.minaintyg.auth.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.DEV_PROFILE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import se.inera.intyg.minaintyg.auth.MinaIntygLoggingSessionRegistryImpl;
import se.inera.intyg.minaintyg.service.monitoring.MonitoringLogService;

@Profile("!" + DEV_PROFILE)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SAMLEntryPoint samlEntryPoint;
    private final SAMLLogoutFilter samlLogoutFilter;
    private final SAMLLogoutProcessingFilter samlLogoutProcessingFilter;
    private final SavedRequestAwareAuthenticationSuccessHandler samlAuthenticationSuccessHandler;
    private final SimpleUrlAuthenticationFailureHandler samlAuthenticationFailureHandler;
    private final MonitoringLogService monitoringService;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final AuthenticationConfiguration configuration;

    private final List<String> profiles;

    public WebSecurityConfig(SAMLEntryPoint samlEntryPoint, SAMLLogoutFilter samlLogoutFilter,
        SAMLLogoutProcessingFilter samlLogoutProcessingFilter,
        @Qualifier("saml") SavedRequestAwareAuthenticationSuccessHandler samlAuthenticationSuccessHandler,
        @Qualifier("saml") SimpleUrlAuthenticationFailureHandler samlAuthenticationFailureHandler, MonitoringLogService monitoringService,
        FindByIndexNameSessionRepository<? extends Session> sessionRepository, AuthenticationConfiguration configuration, Environment environment) {
        this.samlEntryPoint = samlEntryPoint;
        this.samlLogoutFilter = samlLogoutFilter;
        this.samlLogoutProcessingFilter = samlLogoutProcessingFilter;
        this.samlAuthenticationSuccessHandler = samlAuthenticationSuccessHandler;
        this.samlAuthenticationFailureHandler = samlAuthenticationFailureHandler;
        this.monitoringService = monitoringService;
        this.sessionRepository = sessionRepository;
        this.configuration = configuration;
        this.profiles = Arrays.asList(environment.getActiveProfiles());
    }
    @Bean
    public MinaIntygLoggingSessionRegistryImpl loggingSessionRegistry() {
        return new MinaIntygLoggingSessionRegistryImpl(sessionRepository, monitoringService);
    }
    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(loggingSessionRegistry());
    }

    @Bean
    public FilterChainProxy samlFilter() throws Exception {
        List<SecurityFilterChain> chains = new ArrayList<>();
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"), samlEntryPoint));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"), samlLogoutFilter));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"), samlWebSSOProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"), samlLogoutProcessingFilter));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"), metadataDisplayFilter()));
        return new FilterChainProxy(chains);
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public MetadataDisplayFilter metadataDisplayFilter() {
        return new MetadataDisplayFilter();
    }
    @Bean
    public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
        SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
        samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
        samlWebSSOProcessingFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(samlAuthenticationSuccessHandler);
        samlWebSSOProcessingFilter.setAuthenticationFailureHandler(samlAuthenticationFailureHandler);
        return samlWebSSOProcessingFilter;
    }

    @Bean
    public AccessDeniedHandlerImpl accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        //TODO: Update with correct error page
        accessDeniedHandler.setErrorPage("/error?reason=denied");
        return accessDeniedHandler;
    }

    @Bean
    public Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        configureAlwaysPermitted(http);
        configureSaml(http);
        configureCsrf(http);
        configureCors(http);
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }

    private void configureCors(HttpSecurity http) throws Exception {
        http.cors(withDefaults());
    }

    private void configureAlwaysPermitted(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((request) -> {
                request.requestMatchers("/").permitAll();
                request.requestMatchers("/error").permitAll();
                request.requestMatchers("/style/**").permitAll();
                request.requestMatchers("/favicon.ico").permitAll();
                request.requestMatchers("/version").permitAll();
                request.requestMatchers("/api/**").permitAll();
                request.anyRequest().authenticated();
            });
    }

    private void configureSaml(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                request.requestMatchers("/**").fullyAuthenticated()
            )
            .exceptionHandling(exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(samlEntryPoint, new AntPathRequestMatcher("/saml/**"))
            )
            .exceptionHandling(exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE)
            )
            .addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)
            .logout(logoutConfigurer ->
                logoutConfigurer
                .invalidateHttpSession(false)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout-success")
            )
            .sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionAuthenticationStrategy(registerSessionAuthenticationStrategy())
            );
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
    }
}
