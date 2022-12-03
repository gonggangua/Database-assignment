package com.exceptions;

public class DoNotExistException extends Exception {
    public DoNotExistException() {
    }

    @Override
    public String toString() {
        return "Relation do not exist.";
    }
}
