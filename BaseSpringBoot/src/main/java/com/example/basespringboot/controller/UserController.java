package com.example.basespringboot.controller;

import com.example.basespringboot.dto.LoginRequest;
import com.example.basespringboot.dto.RegisterRequest;
import com.example.basespringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody RegisterRequest registerRequest) throws RuntimeException {
        userService.signup(registerRequest);
        return ResponseEntity.ok("Đăng ký tài khoản thành công !");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestBody RegisterRequest registerRequest) throws RuntimeException {
        userService.remove(registerRequest);
        return ResponseEntity.ok("Xóa tài khoản thành công: "+registerRequest.getUser());
    }
}
