package com.example.demo.api.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserService userService;

    public Mono<ServerResponse> getAllUsers(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllUser(),User.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        return userService
                .findById(id)
                .flatMap(user -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(user, User.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<User> user = request.bodyToMono(User.class);

        return user
                .flatMap(u -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userService.createUser(u), User.class)
                );
    }

    public Mono<ServerResponse> updateUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<User> updatedUser = request.bodyToMono(User.class);

        return updatedUser
                .flatMap(u -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userService.updateUser(id, u), User.class)
                );
    }

    public Mono<ServerResponse> deleteUserById(ServerRequest request){
        String id = request.pathVariable("id");
        return userService
                .deleteUser(id)
                .flatMap(u -> ServerResponse
                        .ok().body(u, User.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
