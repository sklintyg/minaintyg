package se.inera.intyg.minaintyg.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.PERSON_ID_ATTRIBUTE;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.TESTABILITY_PROFILE;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUserDetailService;
import se.inera.intyg.minaintyg.auth.Saml2AuthenticationToken;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final MinaIntygUserDetailService minaIntygUserDetailService;
  public static final String ELEG = "eleg";
  public static final String APP_BUNDLE_NAME = "app";

  @Value("${spring.ssl.bundle.jks.app.key.alias}")
  private String alias;
  @Value("${spring.ssl.bundle.jks.app.keystore.password}")
  private String password;
  @Value("${saml.idp.metadata.location}")
  private String samlIdpMetadataLocation;

  private final List<String> activeProfiles;

  public WebSecurityConfig(MinaIntygUserDetailService minaIntygUserDetailService,
      Environment environment) {
    this.minaIntygUserDetailService = minaIntygUserDetailService;
    this.activeProfiles = Arrays.asList(environment.getActiveProfiles());
  }

  @Bean
  public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository(
      SslBundles sslBundles)
      throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {

    final var app = sslBundles.getBundle(APP_BUNDLE_NAME);

    final var appPrivateKey = (PrivateKey) app.getStores()
        .getKeyStore()
        .getKey(alias, password.toCharArray());

    final var appCertificate = (X509Certificate) app.getStores()
        .getKeyStore()
        .getCertificate(alias);

    final var registration = RelyingPartyRegistrations
        .fromMetadataLocation(samlIdpMetadataLocation)
        .registrationId(ELEG)
        .singleLogoutServiceLocation("{baseUrl}/logout/saml2/slo")
        .signingX509Credentials(signing ->
            signing.add(
                Saml2X509Credential.signing(appPrivateKey, appCertificate)
            )
        )
        .build();

    return new InMemoryRelyingPartyRegistrationRepository(registration);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      RelyingPartyRegistrationRepository relyingPartyRegistrationRepository) throws Exception {

    http
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request.
            requestMatchers("/actuator/**").permitAll().
            requestMatchers("/api/testability/**").permitAll()
        )
        .authorizeHttpRequests(request -> request.
            requestMatchers("/api/**").fullyAuthenticated()
        )
        .saml2Login(saml2 -> saml2
            .relyingPartyRegistrationRepository(relyingPartyRegistrationRepository)
            .authenticationManager(
                new ProviderManager(
                    getOpenSaml4AuthenticationProvider()
                )
            )
        )
        .saml2Logout(withDefaults())
        .saml2Metadata(withDefaults())
        .csrf(csrfConfigurer -> csrfConfigurer
            .ignoringRequestMatchers("/api/testability/**")
        );

    if (activeProfiles.contains(TESTABILITY_PROFILE)) {
      // configureFake(http);
    }

    return http.build();
  }

  // https://stackoverflow.com/questions/72508155/spring-saml2-and-spring-session-savedrequest-not-retrieved-cannot-redirect-to
  @Bean
  public DefaultCookieSerializerCustomizer cookieSerializerCustomizer() {
    return cookieSerializer -> cookieSerializer.setSameSite(null);
  }

  private OpenSaml4AuthenticationProvider getOpenSaml4AuthenticationProvider() {
    final var authenticationProvider = new OpenSaml4AuthenticationProvider();
    authenticationProvider.setResponseAuthenticationConverter(responseToken -> {
      final var authentication = OpenSaml4AuthenticationProvider
          .createDefaultResponseAuthenticationConverter()
          .convert(responseToken);
      if (!(authentication != null && authentication.isAuthenticated())) {
        //TODO: Look into better error handling when working with Authentication-jira
        return null;
      }
      final var personId = getAttribute(authentication);
      final var principal = minaIntygUserDetailService.buildPrincipal(personId, LoginMethod.ELVA77);
      return new Saml2AuthenticationToken(principal, authentication);
    });
    return authenticationProvider;
  }

  private String getAttribute(Saml2Authentication samlCredential) {
    final var principal = (DefaultSaml2AuthenticatedPrincipal) samlCredential.getPrincipal();
    final var attributes = principal.getAttributes();
    if (attributes.containsKey(PERSON_ID_ATTRIBUTE)) {
      return (String) attributes.get(PERSON_ID_ATTRIBUTE).get(0);
    }
    throw new IllegalArgumentException(
        "Could not extract attribute '" + PERSON_ID_ATTRIBUTE + "' from Saml2Authentication.");
  }
}
