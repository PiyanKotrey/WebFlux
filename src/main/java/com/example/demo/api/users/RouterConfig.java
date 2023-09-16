package com.example.demo.api.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    RouterFunction<ServerResponse> routes(UserHandler handler) {
        return route(GET("/handler/users").and(accept(MediaType.APPLICATION_JSON)),
                handler::getAllUsers)
                .andRoute(GET("/handler/users/{id}")
                                .and(contentType(MediaType.APPLICATION_JSON)),
                        handler::getUserById)
                .andRoute(POST("/handler/users")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT("/handler/users/{id}")
                                .and(contentType(MediaType.APPLICATION_JSON)),
                        handler::updateUserById)
                .andRoute(DELETE("/handler/users/{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        handler::deleteUserById);
    }

}
