package com.example.demo.api.client;

import com.example.demo.api.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserClientController {
    private final UserClientService userClientService;

    //webClient
    @PostMapping("/postComplex")
    public Mono<User> postComplex(@RequestBody User user){
        return userClientService.postUser(user);
    }
    //    @GetMapping("/clGetAll")
//    public Flux<User> clGetAll(){
//        return userService.clGetAll();
//    }
    @GetMapping("{id}/getOptional")
    public Mono<User> getUserId(@PathVariable String id){
        return userClientService.clGetUserId(id);
    }
    @PutMapping("{id}/clUpdate")
    public Mono<User> clUpdate(@PathVariable String id,
                               @RequestBody User user){
        return userClientService.clUpdateUser(id,user);
    }
    @DeleteMapping("{id}/clDelete")
    public Mono<Void> clDelete(@PathVariable String id){
        return userClientService.clDelete(id);
    }
}
