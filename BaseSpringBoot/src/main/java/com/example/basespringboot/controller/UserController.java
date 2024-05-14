package com.example.basespringboot.controller;

import com.example.basespringboot.dto.LoginRequest;
import com.example.basespringboot.dto.RegisterRequest;
import com.example.basespringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return userService.getAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody RegisterRequest registerRequest) throws RuntimeException {
        userService.signup(registerRequest);
        return ResponseEntity.ok("Success !");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestBody RegisterRequest registerRequest) throws RuntimeException {
        userService.remove(registerRequest);
        return ResponseEntity.ok("Delete success: "+registerRequest.getUserName());
    }
}
