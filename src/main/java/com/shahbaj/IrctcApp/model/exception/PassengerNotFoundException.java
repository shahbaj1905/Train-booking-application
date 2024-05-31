package com.shahbaj.IrctcApp.model.exception;

public class PassengerNotFoundException extends RuntimeException{
    public PassengerNotFoundException(String msg){
        super(msg);
    }
}
