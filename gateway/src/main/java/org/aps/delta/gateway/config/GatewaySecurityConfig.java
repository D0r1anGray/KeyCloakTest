package org.aps.delta.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewaySecurityConfig {

    private final MyFilter myFilter;

    public GatewaySecurityConfig(MyFilter myFilter) {
        this.myFilter = myFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(myFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").hasAuthority("ROLE_eventapp.admin")
                        .requestMatchers("/small/sayHelloAdmin").hasAuthority("ROLE_eventapp.admin")
                        .requestMatchers("/small/sayHelloUser").hasAnyAuthority("ROLE_eventapp.user", "ROLE_eventapp.admin")
                        .requestMatchers("/event/get_events").hasAnyAuthority("ROLE_eventapp.admin", "ROLE_eventapp.user")
                        .requestMatchers("/event/create_event").hasAuthority("ROLE_eventapp.admin")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthenticationConverter());
        return converter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
