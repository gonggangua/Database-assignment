package com.exceptions;

public class NoPermissionException
        extends Exception {
    public NoPermissionException() {
    }

    @Override
    public String toString() {
        return "you have no permission!";
    }
}
