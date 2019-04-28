package com.bdease.spm.ex;

public class ApplicationException extends RuntimeException {    
    private static final long serialVersionUID = 1L;


    public ApplicationException() {
        super();
    }

   
    public ApplicationException(String message) {
        super(message);
    }
}
