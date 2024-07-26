
package com.bookstore.task.controller.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * This handler supplies custom response JSON for authorization failures.
 */
@ControllerAdvice
@Slf4j
public class AuthenticationFailureHandler extends ResponseEntityExceptionHandler {

  /**
   * Generic handler for all web request failures. Specifically, it supplies custom
   * responses for authorization failures.
   *
   * @param ex The thrown exception
   * @param request The web request
   * @return Customized response entity
   */
  @ExceptionHandler({ Exception.class })
  public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
    if (ex instanceof BadCredentialsException) {
      return ResponseEntity.status( HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    return ResponseEntity.status(500).body(ex.getMessage());
  }
}
