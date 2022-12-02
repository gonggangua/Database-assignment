package com.exceptions;

public class BlockedException extends Exception {
    public BlockedException() {
    }

    @Override
    public String toString() {
        return "you are blocked!";
    }
}
