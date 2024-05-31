package com.shahbaj.IrctcApp.model.exception;

public class BookingFailedException extends RuntimeException{

    public BookingFailedException(String msg){
        super(msg);
    }
}
