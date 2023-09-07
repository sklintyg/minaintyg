package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.person.PersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;

@Slf4j
@Service
public class GetPersonFromIntygProxyServiceImpl implements PersonIntegrationService {

    private final WebClient webClient;
    private final String scheme;
    private final String baseUrl;
    private final int port;
    private final String puEndpoint;

    public GetPersonFromIntygProxyServiceImpl(
        @Qualifier(value = "intygProxyWebClient") WebClient webClient,
        @Value("${integration.intygProxy.scheme}") String scheme,
        @Value("${integration.intygProxy.baseurl}") String baseUrl,
        @Value("${integration.intygProxy.port}") int port,
        @Value("${integration.intygProxy.pu.endpoint}") String puEndpoint) {
        this.webClient = webClient;
        this.scheme = scheme;
        this.baseUrl = baseUrl;
        this.port = port;
        this.puEndpoint = puEndpoint;
    }

    @Override
    public PersonResponse getPerson(PersonRequest personRequest) {
        validateRequest(personRequest);
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder
                    .scheme(scheme)
                    .host(baseUrl)
                    .port(port)
                    .path(puEndpoint)
                    .build())
                .body(Mono.just(personRequest), PersonRequest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(PersonResponse.class)
                .share()
                .block();
        } catch (Exception exception) {
            log.error("Not able to get person from intygproxyservice. {}", exception.getMessage());
            throw new RuntimeException("Unable to get person from intygproxyservice");
        }
    }

    private void validateRequest(PersonRequest personRequest) {
        if (personRequest == null || personRequest.getPersonId() == null || personRequest.getPersonId().isEmpty()) {
            log.error("No valid personRequest was provided: {}, cannot get person from intygproxy.", personRequest);
            throw new IllegalArgumentException("Valid personRequest was not provided: " + personRequest);
        }
    }
}
