package com.hptiger.starter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hptiger.starter.ex.ApplicationException;
import com.hptiger.starter.ex.ServerException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monitorjbl.json.JsonResult;

public class BaseController {
    protected  final Logger log = LoggerFactory.getLogger(getClass());
    
    protected JsonResult json = JsonResult.instance();
    
    public class ErrorResponse {
        private String error;

        public ErrorResponse(String errorMessge) {
            this.error = errorMessge;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(Throwable ex) {
        log.error("Unexception error:", ex);
        return new ErrorResponse(ex.getMessage());
    }    
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerException.class)
    public ErrorResponse exception(ServerException e) {
        return new ErrorResponse(e.getMessage());
    }    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse exception(ApplicationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exception(MethodArgumentNotValidException e) {
    	log.error("Argument not valid:",e);  
    	log.error(gson.toJson(e.getBindingResult().getAllErrors()));
        return new ErrorResponse(String.join(";", errors(e)));
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse exception(IllegalStateException e) {
    	log.error("Argument not valid(Illegal):",e);     	
        return new ErrorResponse(e.getMessage());
    }

    private List<String> errors(MethodArgumentNotValidException e) {    	
    	return e.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
    }
    
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

}
