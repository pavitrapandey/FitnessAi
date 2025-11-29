package com.fitU.activityService.Exception;

import com.fitU.activityService.Dto.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponseMessage userNotFoundException(UserNotFoundException ex) {
        return ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

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
