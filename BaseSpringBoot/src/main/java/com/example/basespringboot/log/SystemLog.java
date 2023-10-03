package com.example.basespringboot.log;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;

public class SystemLog {
    private static final Logger LOGGER = LogManager.getLogger(SystemLog.class);

    public static void log(Object object, TypeLog type) {
        /**
         * Log json to elk, example:
         {
         "time": "Sep 29, 2023, 2:48:10 PM",
         "type": "response",
         "uuid": "9efa6b14-7b92-4488-84c0-0d5b75a97fe6",
         "path": "/api/read-card-fpt",
         "user": "livebank",
         "body": "{status=0, message=Success!, uuid=a025f9da-8c98-47de-9bb7-537a9cc6b99c,...}"
         }
         */
        LogDto logDto = new LogDto();
        logDto.setTime(new Timestamp(System.currentTimeMillis()));
        logDto.setTypeLog(type.getType());
        logDto.setUuid(ThreadContext.get("uuid"));
        logDto.setPath(ThreadContext.get("path"));
        logDto.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
        logDto.setBody(object);

        LOGGER.info(new Gson().toJson(logDto));
    }

}
