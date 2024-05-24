package pl.akademiaqa.bos;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + " " + x.getDefaultMessage())
                .collect(Collectors.toList());
        return handleError(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handle(IllegalArgumentException ex) {
        return handleError(HttpStatus.BAD_REQUEST, List.of(ex.getMessage().split("\n")));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handle(IllegalStateException ex) {
        return handleError(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handle(AccessDeniedException ex) {
        return handleError(HttpStatus.FORBIDDEN, List.of(ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handle(ValidationException ex) {
        return handleError(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handle(PSQLException ex) {
        return handleError(HttpStatus.CONFLICT, List.of("operation could not be performed"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handle(HttpMessageNotReadableException ex) {
        return handleError(HttpStatus.BAD_REQUEST, List.of("operation could not be performed"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handle(DataIntegrityViolationException ex) {
        return handleError(HttpStatus.CONFLICT, List.of("operation could not be performed"));
    }

    private ResponseEntity<Object> handleError(HttpStatus status, List<String> errorMessage) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", errorMessage);
        return new ResponseEntity<>(body, status);
    }
}
