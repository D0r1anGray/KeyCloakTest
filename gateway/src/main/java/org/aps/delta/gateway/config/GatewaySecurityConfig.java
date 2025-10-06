package org.aps.delta.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {
    private final MyFilter myFilter;

    public GatewaySecurityConfig(MyFilter myFilter) {
        this.myFilter = myFilter;
    }
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .addFilterBefore((exchange, chain) -> {
                    ;
                }, SecurityWebFilterChain.class)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/small/sayHelloAdmin").hasAuthority("ROLE_eventapp.admin")
                        .pathMatchers("/small/sayHelloUser").hasAnyAuthority("ROLE_eventapp.user", "ROLE_eventapp" +
                                ".admin")
                        .pathMatchers("/event/**").hasAnyAuthority("ROLE_eventapp.admin")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                );
        return http.build();
    }

    //todo Сделать кастомный фильтр
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri("http://localhost:9090/realms/delta/protocol/openid-connect/certs")
                .jwsAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

    private ReactiveJwtAuthenticationConverterAdapter grantedAuthoritiesExtractor() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthenticationConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
