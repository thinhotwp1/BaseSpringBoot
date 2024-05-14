package com.example.basespringboot.controller;

import com.example.basespringboot.service.AnyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
//@PreAuthorize("hasAuthority('admin') or hasAuthority('any')")
public class AnyController {

    @Autowired
    AnyService anyService;

    @PostMapping("/post")
    public ResponseEntity<?> getName(@RequestBody int number){
        if(number==5) throw new RuntimeException("Hello");
        return ResponseEntity.ok(anyService.getName(number));
    }
}
