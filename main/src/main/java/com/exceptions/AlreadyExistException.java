package com.exceptions;

public class AlreadyExistException extends Exception {
    public AlreadyExistException() {
    }

    @Override
    public String toString() {
        return "Relation already exists.";
    }
}
