package com.exceptions;

public class LevelLimitException extends Exception{
    public LevelLimitException() {
    }

    @Override
    public String toString() {
        return "you cannot create more servers than your level!";
    }
}
