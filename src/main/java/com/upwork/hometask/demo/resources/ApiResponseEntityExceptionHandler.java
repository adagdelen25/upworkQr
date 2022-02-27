package com.upwork.hometask.demo.resources;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.upwork.hometask.demo.exception.BadRequestException;
import com.upwork.hometask.demo.exception.RuleNotFoundException;
import com.upwork.hometask.demo.models.resp.BadRequestResponse;
import com.upwork.hometask.demo.models.resp.ResponseError;
import com.upwork.hometask.demo.models.resp.ServerErrorResponse;
import com.upwork.hometask.demo.models.resp.VgpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public VgpResponse handleServerException(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return new ServerErrorResponse(ex.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  public VgpResponse handleNullPointerException(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return new ServerErrorResponse(ExceptionUtils.getStackTrace(ex));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public VgpResponse handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return new BadRequestResponse(ex.getMessage());
  }
  @ExceptionHandler(RuleNotFoundException.class)
  public VgpResponse handleEntityNotFoundException(RuleNotFoundException ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return new BadRequestResponse(ex.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(ex.getMessage(), ex);
    List<ResponseError> errors =
        ex
            .getBindingResult()
            // .getAllErrors().stream()
            // .map(e -> new ResponseError(e.getDefaultMessage(), ((FieldError) e).getField()))
            .getFieldErrors()
            .stream()
            .map(e -> new ResponseError(e.getDefaultMessage(), e.getField()))
            .collect(Collectors.toList());

    return new BadRequestResponse(errors);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return new BadRequestResponse(ex.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.error(ex.getMessage(), ex);
    Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
    List<ResponseError> errors =
        constraintViolations.stream()
            .map(
                constraintViolation ->
                    new ResponseError(
                        constraintViolation.getMessage(),
                        constraintViolation.getPropertyPath().toString()))
            .collect(Collectors.toList());
    return new BadRequestResponse(errors);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public VgpResponse handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, WebRequest request) {
    for (Throwable throwable = ex.getCause(); throwable != null; throwable = throwable.getCause()) {
      if (!Objects.isNull(throwable.getCause())) {
        continue;
      }
      log.error(throwable.getMessage(), throwable);
      return new BadRequestResponse(throwable.getMessage());
    }

    return new BadRequestResponse(ex.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public VgpResponse handleBadRequestException(BadRequestException ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return new BadRequestResponse(ex.getMessage());
  }
}
