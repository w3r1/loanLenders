package com.loanlenders.exception;

public class UnsufficientAvailableLoans extends RuntimeException {

    public UnsufficientAvailableLoans(String message) {
        super(message);
    }
}
