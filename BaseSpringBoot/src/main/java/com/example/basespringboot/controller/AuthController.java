package com.example.basespringboot.controller;

import com.example.basespringboot.dto.LoginRequest;
import com.example.basespringboot.dto.RegisterRequest;
import com.example.basespringboot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest){
        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.signup(registerRequest);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.remove(registerRequest);
            return new ResponseEntity<>("Remove successfully user:"+registerRequest.getUser(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
