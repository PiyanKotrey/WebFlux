package com.example.demo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public UserService(WebClient.Builder webClientBuilder, UserRepository userRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
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
    public Mono<String> postUser(User user){
        return webClient
                .post()
                .uri("/user")
                .body(Mono.just(user),User.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getUserOptional(){
        return webClient
                .get()
                .uri("user/2222")
                .retrieve()
                .bodyToMono(String.class);
    }

}
