package com.example.demo.controller;

import com.example.demo.common.Const;
import com.example.demo.domain.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class CommonController {
  protected <T>ResponseEntity<?> toSuccessResult(T data){
    ResponseMessage<T> message = new ResponseMessage<>();
    message.setCode(Const.RETURN_CODE_SUCCESS);
    message.setSuccess(Const.RESPONSE_STATUS_TRUE);
    message.setData(data);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  protected <T> ResponseEntity<?> toExceptionResult(String errorMessage, String code){
    ResponseMessage<T> message = new ResponseMessage<>();
    message.setCode(code);
    message.setSuccess(Const.RESPONSE_STATUS_FAIL);
    message.setDescription(errorMessage);
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }
}
