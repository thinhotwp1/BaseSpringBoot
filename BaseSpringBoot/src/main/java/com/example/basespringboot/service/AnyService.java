package com.example.basespringboot.service;

import com.example.basespringboot.rest.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnyService {
    public ResponseData<?> getName(int number) {
        return new ResponseData<>().success(number);
    }
}
