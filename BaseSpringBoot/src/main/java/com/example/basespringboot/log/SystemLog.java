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
         * {
         * 	"time": "Oct 2, 2023, 9:23:54 AM",
         * 	"type": "response",
         * 	"uuid": "2ca8bc8e-be2b-4c6b-b54f-e9760eaeb813",
         * 	"path": "/api/post",
         * 	"user": "anonymousUser",
         * 	"body": {
         * 		"status": 0,
         * 		"message": "Success!",
         * 		"timeResponse": "Oct 2, 2023, 9:23:54 AM",
         * 		"uuid": "2ca8bc8e-be2b-4c6b-b54f-e9760eaeb813",
         * 		"duration": 6,
         * 		"path": "/api/post",
         * 		"data": 10
         *      }
         * }
         */
        LOGGER.info("{"
                + "\"time\": " + new Gson().toJson(new Timestamp(System.currentTimeMillis()))
                + ", \"type\": \"" + type.getType()
                + "\", \"uuid\": \"" + ThreadContext.get("uuid")
                + "\", \"path\": \"" + ThreadContext.get("path")
                + "\", \"user\": \"" + SecurityContextHolder.getContext().getAuthentication().getName()
                + "\", \"body\": " + new Gson().toJson(object)
                + "}"
        );
    }

}
