package com.example.basespringboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Oauth2Controller {
    @GetMapping("")
    public ResponseEntity<?> login(OAuth2AuthenticationToken token){
        return ResponseEntity.ok(token.getPrincipal().getAttributes());
    }
}
