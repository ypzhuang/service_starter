package com.bdease.spm.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bdease.spm.ex.ApplicationException;
import com.bdease.spm.ex.ServerException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monitorjbl.json.JsonResult;

public class BaseController {
    protected  final Logger log = LoggerFactory.getLogger(getClass());
    
    protected JsonResult json = JsonResult.instance();
    
    class ErrorResponse {
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
    
    class MapResponse {
        private  Map<String,String> keyTitles;
        private List<? extends Object> list = new ArrayList<>();
        
        public MapResponse(Map<String,String> keyTitles,List<? extends Object> list){
            this.keyTitles = keyTitles;
            this.list  = list;
        }

        public Map<String, String> getKeyTitles() {
            return keyTitles;
        }

        public void setKeyTitles(Map<String, String> keyTitles) {
            this.keyTitles = keyTitles;
        }

        public List<? extends Object> getList() {
            return list;
        }

        public void setList(List<? extends Object> list) {
            this.list = list;
        }
    }
    
    class PageResponse extends MapResponse {
        private Integer page;
        private Integer total;

        public PageResponse(Map<String, String> keyTitles, List<? extends Object> list) {
            super(keyTitles, list);
        }
        
        public PageResponse(Map<String, String> keyTitles, List<? extends Object> list, Integer page, Integer total) {
            super(keyTitles, list);
            this.setPage(page);
            this.setTotal(total);
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
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
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResponse exception(UsernameNotFoundException e) {
        log.error("用户名或密码错误。");
        return new ErrorResponse("用户名或密码错误。");
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse exception(BadCredentialsException e) {
        log.error("用户名或密码错误。");
        return new ErrorResponse("用户名或密码错误。");
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DisabledException.class)
    public ErrorResponse exception(DisabledException e) {
        log.error(e.getMessage(),e);
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
    
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();;
   
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse exception(AccessDeniedException e) {
    	log.error("Access Denied Exception:",e);  
        return new ErrorResponse(e.getMessage());
    }   
}
