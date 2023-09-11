package com.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of("/user/register", "/user/authenticate", "/user/validate","product/getProducts/");

    private Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> openApiEndPoints.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));

    public Predicate<ServerHttpRequest> getIsSecured() {
        return isSecured;
    }
}
