package com.voxloud.provisioning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneratingFileException extends RuntimeException {
    public GeneratingFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
