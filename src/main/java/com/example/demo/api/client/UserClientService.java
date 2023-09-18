package com.example.demo.api.client;

import com.example.demo.api.users.User;
import com.example.demo.error.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class UserClientService {
    private final WebClient webClient;
    @Autowired
    public UserClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();

    }

    //using web client
    public Mono<User> postUser(User user){
        return webClient
                .post()
                .uri("/user")
                .body(Mono.just(user),User.class)
                .retrieve()
                .bodyToMono(User.class);
    }

    //    public Flux<User> clGetAll(){
//        return webClient
//                .get()
//                .uri("/user")
//                .retrieve()
//                .onStatus(httpStatus->!httpStatus.is2xxSuccessful(),
//                        clientResponse -> handleError((HttpStatus) clientResponse.statusCode()))
//                .bodyToMono(User.class);
//    }
    public Mono<User> clGetUserId(String id){
        return webClient
                .get()
                .uri("/user/"+id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse ->Mono.error(new Error("client Error")) )
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse ->Mono.error(new Error("server Error")) )
                .bodyToMono(User.class);
    }

    public Mono<User> clUpdateUser(String id,User user){
        return webClient
                .put()
                .uri("/user/"+id)
                .body(Mono.just(user),User.class)
                .retrieve()
                .bodyToMono(User.class);

    }
    public Mono<Void> clDelete(String id){
        return webClient
                .delete()
                .uri("/user/"+id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
