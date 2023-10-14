package com.example.basespringboot.service;

import com.example.basespringboot.rest.ResponseData;
import com.example.basespringboot.rest.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnyService {

    @Autowired
    RestClient restClient;

    public ResponseData<?> getName(int number) {
        return new ResponseData<>().success(number);
    }

    public Object callRestAPI(Object request) throws Exception {
        restClient.setUrl("https://path-you-want");
        return restClient.postObject(request,Object.class);
    }
}
