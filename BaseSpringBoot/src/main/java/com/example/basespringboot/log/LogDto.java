package com.example.basespringboot.log;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LogDto {
    private Timestamp time;
    private String typeLog;
    private String uuid;
    private String user;
    private String path;
    private Object body;
}
