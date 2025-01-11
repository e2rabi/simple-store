package ma.errabi.sdk.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ma.errabi.sdk.exception.EntityNotFoundException;
import ma.errabi.sdk.exception.HttpErrorInfo;
import ma.errabi.sdk.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(InvalidInputException ex, HttpServletRequest request){
        log.info("Invalid request: {} {}", request.getMethod(), request.getRequestURI());
        return new HttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY,request.getRequestURI(),ex.getMessage());
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundEntity(EntityNotFoundException ex, HttpServletRequest request){
        log.error("Resource not found: {} {}", request.getMethod(), request.getRequestURI());
        return new HttpErrorInfo(HttpStatus.NOT_FOUND,request.getRequestURI(),ex.getMessage());
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody HttpErrorInfo handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request){
        log.error("Invalid request: {} {}", request.getMethod(), request.getRequestURI());
        return new HttpErrorInfo(HttpStatus.NOT_FOUND,request.getRequestURI(),ex.getMessage());
    }
}
