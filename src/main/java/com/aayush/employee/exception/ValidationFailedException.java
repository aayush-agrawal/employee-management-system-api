package com.aayush.employee.exception;

public class ValidationFailedException extends RuntimeException {

  public ValidationFailedException(String message){
    super(message);
  }

}
