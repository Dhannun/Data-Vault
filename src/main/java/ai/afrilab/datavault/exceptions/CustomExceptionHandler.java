package ai.afrilab.datavault.exceptions;

import ai.afrilab.datavault.exceptions.dto.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.HashSet;
import java.util.Set;

import static ai.afrilab.datavault.exceptions.enums.ErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomExceptionHandler {


  private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

  @ExceptionHandler(LockedException.class)
  public final ResponseEntity<ExceptionResponse> handleLockedException(LockedException exception) {
    return ResponseEntity
        .status(UNAUTHORIZED)
        .body(
            ExceptionResponse.builder()
                .errorCode(ACCOUNT_LOCKED.getCode())
                .errorDescription(ACCOUNT_LOCKED.getDescription())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(SignatureException.class)
  public final ResponseEntity<ExceptionResponse> handleSignatureException(SignatureException exception) {
    return ResponseEntity
        .status(FORBIDDEN)
        .body(
            ExceptionResponse.builder()
                .errorCode(FORBIDDEN.value())
                .errorDescription(exception.getMessage())
                .error("Token Signature Exception")
                .build()
        );
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public final ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException exception) {
    return ResponseEntity
        .status(FORBIDDEN)
        .body(
            ExceptionResponse.builder()
                .errorCode(FORBIDDEN.value())
                .errorDescription(exception.getMessage())
                .error("Token Expired Exception")
                .build()
        );
  }


  @ExceptionHandler(DisabledException.class)
  public final ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException exception) {
    return ResponseEntity
        .status(UNAUTHORIZED)
        .body(
            ExceptionResponse.builder()
                .errorCode(ACCOUNT_DISABLED.getCode())
                .errorDescription(ACCOUNT_DISABLED.getDescription())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(BadCredentialsException.class)
  public final ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException exception) {
    log.error(exception.getMessage());
    return ResponseEntity
        .status(UNAUTHORIZED)
        .body(
            ExceptionResponse.builder()
                .errorCode(BAD_CREDENTIALS.getCode())
                .errorDescription(BAD_CREDENTIALS.getDescription())
                .error(BAD_CREDENTIALS.getDescription())
                .build()
        );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
    return ResponseEntity
        .status(NOT_FOUND)
        .body(
            ExceptionResponse.builder()
                .errorCode(NOT_FOUND.value())
                .errorDescription(exception.getMessage())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(InvalidResourceException.class)
  public final ResponseEntity<ExceptionResponse> handleInvalidResourceException(InvalidResourceException exception) {
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(
            ExceptionResponse.builder()
                .errorCode(BAD_REQUEST.value())
                .errorDescription(exception.getMessage())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(UserExistsByUsername.class)
  public final ResponseEntity<ExceptionResponse> handleUserExistsByUsername(UserExistsByUsername exception) {
    return ResponseEntity
        .status(CONFLICT)
        .body(
            ExceptionResponse.builder()
                .errorCode(USER_EXISTS_BY_USERNAME.getCode())
                .errorDescription(exception.getMessage())
                .error(USER_EXISTS_BY_USERNAME.getDescription())
                .build()
        );
  }

  @ExceptionHandler(ResourceExistsException.class)
  public final ResponseEntity<ExceptionResponse> handleResourceExistsException(ResourceExistsException exception) {
    return ResponseEntity
        .status(CONFLICT)
        .body(
            ExceptionResponse.builder()
                .errorCode(CONFLICT.value())
                .errorDescription(exception.getMessage())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(OperationFailedException.class)
  public final ResponseEntity<ExceptionResponse> handleOperationFailedException(OperationFailedException exception) {
    return ResponseEntity
        .status(EXPECTATION_FAILED)
        .body(
            ExceptionResponse.builder()
                .errorCode(EXPECTATION_FAILED.value())
                .error(exception.getMessage())
                .build()
        );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    Set<String> errors = new HashSet<>();

    exception.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> errors.add(error.getDefaultMessage())
        );

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(
            ExceptionResponse.builder()
                .validationErrors(errors)
                .build()
        );
  }


//  @ExceptionHandler(Exception.class)
//  public final ResponseEntity<ExceptionResponse> handleException(Exception exception, HttpServletResponse response) {
//
//    // If the response is already committed (i.e., already sent to the client), do not process further
//    if (response.isCommitted()) {
//      log.warn("Response already committed. Skipping exception handling for: {}", exception.getMessage());
//      return null;
//    }
//
//    log.error(exception.getMessage());
//    log.error(Arrays.toString(exception.getStackTrace()));
//    return ResponseEntity
//        .status(INTERNAL_SERVER_ERROR)
//        .body(
//            ExceptionResponse.builder()
//                .errorDescription(exception.getMessage())
//                .error("Internal Server Error, please contact the administrator")
//                .build()
//        );
//  }
}
