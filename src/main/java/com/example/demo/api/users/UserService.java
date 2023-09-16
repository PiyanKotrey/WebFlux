package com.example.demo.api.users;

import com.example.demo.error.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final WebClient webClient;
    @Autowired
    public UserService(WebClient.Builder webClientBuilder,
                       UserRepository userRepository,
                       ReactiveMongoTemplate reactiveMongoTemplate) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api").build();
        this.userRepository = userRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    private final UserRepository userRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;



    public Mono<User> createUser(User user){
        return userRepository.save(user);
    }

    public Flux<User> getAllUser(){
        return userRepository.findAll();
    }

    public Mono<User> findById(String id){
        return userRepository.findById(id);
    }

    public Mono<User> updateUser(String id,User user){
        return userRepository.findById(id)
                .flatMap(dbUser->{
            dbUser.setAge(user.getAge());
            dbUser.setSalary(user.getSalary());
            return userRepository.save(dbUser);
                });
    }

    public Mono<User> deleteUser(String id){
        return userRepository.findById(id).flatMap(existingUser->
                userRepository.delete(existingUser).then(Mono.just(existingUser))
        );
    }

    public Flux<User> fetchUsers(String name) {
        Query query = new Query()
                .with(Sort
                        .by(Collections.singletonList(Sort.Order.asc("age")))
                );
        query.addCriteria(Criteria
                .where("name")
                .regex(name)
        );

        return reactiveMongoTemplate
                .find(query, User.class);
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
