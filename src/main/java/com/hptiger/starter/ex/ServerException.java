package com.hptiger.starter.ex;

public class ServerException extends RuntimeException {    
    private static final long serialVersionUID = 1L;


    public ServerException() {
        super();
    }

   
    public ServerException(String message) {
        super(message);
    }
}
