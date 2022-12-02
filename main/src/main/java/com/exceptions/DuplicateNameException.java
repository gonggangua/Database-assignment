package com.exceptions;

public class DuplicateNameException extends Exception {
    private String name;

    public DuplicateNameException(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " is duplicate.";
    }
}
