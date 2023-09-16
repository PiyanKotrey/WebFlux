package com.example.demo.api.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping
    public Flux<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id){
        Mono<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUserById(@PathVariable String id, @RequestBody User user){
        return userService.updateUser(id,user)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String id){
        return userService.deleteUser(id)
                .map(r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
    public Flux<User> searchUsers(@RequestParam("name") String name) {
        return userService.fetchUsers(name);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamAllUsers() {
        return userService
                .getAllUser()
                .flatMap(user -> Flux
                        .zip(Flux.interval(Duration.ofSeconds(3)),
                                Flux.fromStream(Stream.generate(() -> user))
                        )
                        .map(Tuple2::getT2)
                );
    }

    //webClient
    @PostMapping("/postComplex")
    public Mono<User> postComplex(@RequestBody User user){
        return userService.postUser(user);
    }
//    @GetMapping("/clGetAll")
//    public Flux<User> clGetAll(){
//        return userService.clGetAll();
//    }
    @GetMapping("{id}/getOptionalComplex")
    public Mono<User> getUserId(@PathVariable String id){
        return userService.clGetUserId(id);
    }
    @PutMapping("{id}/clUpdate")
    public Mono<User> clUpdate(@PathVariable String id,
                               @RequestBody User user){
        return userService.clUpdateUser(id,user);
    }
    @DeleteMapping("{id}/clDelete")
    public Mono<Void> clDelete(@PathVariable String id){
        return userService.clDelete(id);
    }


}
