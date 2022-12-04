package com.exceptions;

public class NoPermissionException
        extends Exception {
    String action;

    public NoPermissionException(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "you have no permission to " + action + ".";
    }
}
