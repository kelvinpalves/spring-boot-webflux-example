package br.com.forgeit.note.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${baseUrl}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(clientHttpConnector())
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    private ClientHttpConnector clientHttpConnector() {
        HttpClient httpClient = HttpClient.create()
                .wiretap(true); // This enables detailed HTTP request/response logging
        return new ReactorClientHttpConnector(httpClient);
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.debug("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers().asHttpHeaders()
                    .forEach((name, values) -> values.forEach(value -> logger.debug("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }
}
