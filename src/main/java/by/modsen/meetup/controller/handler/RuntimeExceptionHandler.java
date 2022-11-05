package by.modsen.meetup.controller.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RuntimeExceptionHandler {

    public static final Logger LOGGER = LogManager.getLogger(RuntimeExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(RuntimeException e) {
        LOGGER.warn(e.getMessage());
        return "Please try again letter";
    }
}
