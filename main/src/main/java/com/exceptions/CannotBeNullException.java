package com.exceptions;

public class CannotBeNullException extends Exception {
    String type;

    public CannotBeNullException(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " cannot be null.";
    }
}
