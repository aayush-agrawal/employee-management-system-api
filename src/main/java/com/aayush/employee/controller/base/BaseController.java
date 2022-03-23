package com.aayush.employee.controller.base;

import com.aayush.employee.exception.ValidationFailedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class BaseController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationFailedException.class)
  @ResponseBody
  ErrorResult handleMethodArgumentNotValidException(ValidationFailedException e) {
    ErrorResult errorResult = new ErrorResult(e.getMessage());
    return errorResult;
  }

  @Getter
  @AllArgsConstructor
  static class ErrorResult {
    private final String error;
  }

}
