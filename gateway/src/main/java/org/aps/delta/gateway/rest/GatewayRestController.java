package org.aps.delta.gateway.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
public class GatewayRestController {

    private final RestTemplate restTemplate;

    @Value("${gateway.routes.eventapp}")
    private String eventappUri;

    @Value("${gateway.routes.smallservice}")
    private String smallserviceUri;

    public GatewayRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/event.**")
    public ResponseEntity<String> routeToEventapp(HttpServletRequest request) {
        String path = request.getRequestURI();
        String targerUrl = eventappUri + path;
        return forwardRequest(request, targerUrl);
    }

    @RequestMapping("/small/**")
    public ResponseEntity<String> routeToSmallservice(HttpServletRequest request) {
        String path = request.getRequestURI();
        String targerUrl = smallserviceUri + path;
        return forwardRequest(request, targerUrl);
    }

    private ResponseEntity<String> forwardRequest(HttpServletRequest request, String targetUrl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", request.getHeader("Authorization"));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
        return restTemplate.exchange(targetUrl, HttpMethod.valueOf(request.getMethod()), httpEntity, String.class);
    }
}

