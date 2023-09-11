package com.apigateway.filter;

import com.apigateway.exception.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import com.apigateway.util.JwtUtil;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.getIsSecured().test(exchange.getRequest())) {

                RequestPath path = exchange.getRequest().getPath();

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                        boolean isValidUser = jwtUtil.validateToken(authHeader,path);
                        if (isValidUser) log.info("User is authorized");
                        else throw new NotAuthorizedException("User is not authorized");

                } catch (Exception e) {
                    throw new NotAuthorizedException("Invalid access");
                }

            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
