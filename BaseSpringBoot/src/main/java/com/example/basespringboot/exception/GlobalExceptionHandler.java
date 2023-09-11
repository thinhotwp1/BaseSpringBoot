package com.example.basespringboot.exception;

import com.example.basespringboot.resttemplate.ResponseData;
import com.example.basespringboot.util.StringUtil;
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
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "File đã tồn tại trong hệ thống"));
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handler(ValidationException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, e.getMessage()));
    }

    @ExceptionHandler({InvalidAlgorithmParameterException.class})
    public ResponseEntity<?> handler(InvalidAlgorithmParameterException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, e.getMessage()));
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<?> handler(IOException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "Không có file hoặc không đúng định dạng"));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handler(AccessDeniedException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseData.fail(403, "Không được phép thực hiện"));
    }

    @ExceptionHandler({InvalidObjectException.class})
    public ResponseEntity<?> handler(InvalidObjectException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "File không hợp lệ"));
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<?> handler(NumberFormatException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "Không đúng số lượng"));
    }

    @ExceptionHandler({InvalidFileNameException.class})
    public ResponseEntity<?> handler(InvalidFileNameException e) {
        logErrorDetail(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.fail(400, "File không hợp lệ"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnwantedException(Exception e) {
        logErrorDetail(e);
        return ResponseEntity.ok().body(ResponseData.fail(519, "Có lỗi xảy ra trong quá trình xử lý: " + e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handler(RuntimeException e) {
        logErrorDetail(e);
        return ResponseEntity.status(520).body(ResponseData.fail(520, "Có lỗi Runtime trong quá trình xử lý: " + e.getMessage()));
    }

    private void logErrorDetail(Exception e){
        // log Error Detail
//        log.info("Error detail: "+ e.getMessage());
    }
}
