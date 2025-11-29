package com.fitUAI.userService.Exception;
import com.fitUAI.userService.Dto.ApiResponseMessage;
import com.fitUAI.userService.Exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ApiResponseMessage emailAlreadyExistException(UserAlreadyExistException ex) {
        return ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    //UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponseMessage userNotFoundException(UserNotFoundException ex) {
        return ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrorList= ex.getBindingResult().getAllErrors();
        Map<String, Object> response= new HashMap<>();
        objectErrorList.stream().forEach(objectError-> {
            String message= objectError.getDefaultMessage();
            String fieldName= ((FieldError)objectError).getField();
            response.put(fieldName,message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}