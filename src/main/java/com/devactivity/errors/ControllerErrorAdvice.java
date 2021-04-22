package com.devactivity.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.xml.sax.SAXParseException;

@ControllerAdvice
public class ControllerErrorAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound() {
        return "error/user-not-found";
    }

    @ExceptionHandler(SAXParseException.class)
    public String handleInvalidRssUrl() {
        return "error/rss-not-found";
    }


}
