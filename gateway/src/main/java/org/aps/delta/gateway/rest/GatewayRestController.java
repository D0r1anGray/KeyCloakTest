package org.aps.delta.gateway.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

public class GatewayRestController {

    @GetMapping("/call-small-admin")
    public String callSmallAdmin(Authentication authentication) {
        String token = ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
        WebClient webClient = WebClient.create("http://localhost:8092");
        return webClient.get()
                .uri("/small/sayHelloAdmin")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/call-small-user")
    public String callSmallUser(Authentication authentication) {
        String token = ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
        WebClient webClient = WebClient.create("http://localhost:8092");
        return webClient.get()
                .uri("/small/sayHelloUser")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
