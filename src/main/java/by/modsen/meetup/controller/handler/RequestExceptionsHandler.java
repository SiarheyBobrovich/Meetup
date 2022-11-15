package by.modsen.meetup.controller.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.OptimisticLockException;
import javax.validation.ValidationException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RequestExceptionsHandler {

    public static final Logger LOGGER = LogManager.getLogger("RequestExceptionsHandler");

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(ValidationException e) {
        LOGGER.info(e.getLocalizedMessage());
        final HashMap<String, String> responseMessages = new HashMap<>();

        //messages like: "save.meetup.place: must be only alphabetic or spaces, ..."
        String[] messages = e.getMessage().split(",");

        for (String message : messages) {
            int startMessageIndex = message.indexOf(":");
            int startFieldName = message.lastIndexOf(".");

            responseMessages.put(
                    message.substring(startFieldName + 1, startMessageIndex),
                    message.substring(startMessageIndex + 2)
            );
        }

        return responseMessages;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public String handle(OptimisticLockException e) {
        LOGGER.info(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String handle(HttpMessageNotReadableException e) {
        return e.getMostSpecificCause().getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handle(DateTimeParseException e) {
        LOGGER.info(e.getMessage());
        return "date must be like: 2000-10-31";
    }


}
