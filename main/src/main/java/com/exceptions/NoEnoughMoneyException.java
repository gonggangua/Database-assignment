package com.exceptions;

public class NoEnoughMoneyException extends Exception {
    public NoEnoughMoneyException() {
    }

    @Override
    public String toString() {
        return "you don't have enough money to upgrade";
    }
}
