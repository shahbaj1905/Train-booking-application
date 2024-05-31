package com.shahbaj.IrctcApp.model.exception;

public class SeatNotFoundException extends RuntimeException{
    public SeatNotFoundException(String msg){
        super(msg);
    }
}
