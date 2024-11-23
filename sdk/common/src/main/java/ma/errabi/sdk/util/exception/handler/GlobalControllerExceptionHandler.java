package ma.errabi.sdk.util.exception.handler;

import ma.errabi.sdk.util.exception.HttpErrorInfo;
import ma.errabi.sdk.util.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private final Logger log = Logger.getLogger(GlobalControllerExceptionHandler.class.getName());

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(InvalidInputException ex, ServerHttpRequest request){
        log.info("Invalid request: " + request.getMethod() + " " + request.getURI());
        return new HttpErrorInfo();
    }
}
