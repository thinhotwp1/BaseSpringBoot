package com.example.basespringboot.exception;

import com.example.basespringboot.resttemplate.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.security.InvalidAlgorithmParameterException;
import java.util.UUID;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler({FileAlreadyExistsException.class})
    public ResponseEntity<?> handler(FileAlreadyExistsException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "File đã tồn tại trong hệ thống"));
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handler(ValidationException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, e.getMessage()));
    }

    @ExceptionHandler({InvalidAlgorithmParameterException.class})
    public ResponseEntity<?> handler(InvalidAlgorithmParameterException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, e.getMessage()));
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<?> handler(IOException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "Không có file hoặc không đúng định dạng"));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handler(AccessDeniedException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseData.fail(403, "Không được phép thực hiện"));
    }

    @ExceptionHandler({InvalidObjectException.class})
    public ResponseEntity<?> handler(InvalidObjectException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "File không hợp lệ"));
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<?> handler(NumberFormatException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "Không đúng số lượng"));
    }

    @ExceptionHandler({InvalidFileNameException.class})
    public ResponseEntity<?> handler(InvalidFileNameException e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "File không hợp lệ"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnwantedException(Exception e) {
        initLogging();
        logErrorDetail(e);
        return ResponseEntity.ok().body(ResponseData.fail(1000, "Có lỗi xảy ra trong quá trình xử lý: " + e.getMessage()));
    }

    private void initLogging(){
        ThreadContext.put("uuid", UUID.randomUUID().toString());
        ThreadContext.put("path",request.getRequestURI());
    }

    private void logErrorDetail(Exception e){
        // log Error Detail
        log.info("______________________________________________________________________________________________________________________________________");
        log.info("UUID: " + ThreadContext.get("uuid"));
        log.info("PATH: " + ThreadContext.get("path"));
        // Nếu sử dụng cùng với Security và sử dụng authentication thì gán username vào
        log.info("Username: " + SecurityContextHolder.getContext().getAuthentication().getName());
//        log.info("Request: " + request.get);
        log.info("Error detail: "+ e.getMessage());
    }
}
