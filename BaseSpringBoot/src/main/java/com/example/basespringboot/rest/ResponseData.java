package com.example.basespringboot.rest;

import com.example.basespringboot.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.logging.log4j.ThreadContext;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class ResponseData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5435628560059929L;

    private int status;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+7")
    private final Date timeResponse;
    private String uuid;
    private long duration;
    private String path;
    private T data;

    public ResponseData() {
        this.timeResponse = new Date();
    }

    public ResponseData<T> success(T data) {
        this.data = data;
        this.status = 0;
        this.message = "Success!";
        this.path = ThreadContext.get("path");
        this.uuid = ThreadContext.get("uuid");
        long start = Long.parseLong(StringUtil.isNullOrEmpty(ThreadContext.get("startTime")) ? "0" : ThreadContext.get("startTime"));
        this.duration = System.currentTimeMillis() - start;
        return this;
    }

    public ResponseData<T> error(int code, String message) {
        this.status = code;
        this.message = message;
        this.path = ThreadContext.get("path");
        this.uuid = ThreadContext.get("uuid");
        long start = Long.parseLong(StringUtil.isNullOrEmpty(ThreadContext.get("startTime")) ? "0" : ThreadContext.get("startTime"));
        this.duration = System.currentTimeMillis() - start;
        return this;
    }

    public ResponseData<T> error(int code, String message, T data) {
        this.status = code;
        this.message = message;
        this.data = data;
        this.path = ThreadContext.get("path");
        this.uuid = ThreadContext.get("uuid");
        long start = Long.parseLong(StringUtil.isNullOrEmpty(ThreadContext.get("startTime")) ? "0" : ThreadContext.get("startTime"));
        this.duration = System.currentTimeMillis() - start;
        return this;
    }

}
