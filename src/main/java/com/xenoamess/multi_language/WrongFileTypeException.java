package com.xenoamess.multi_language;

public class WrongFileTypeException extends RuntimeException {
    public WrongFileTypeException() {
        super();
    }

    public WrongFileTypeException(String string) {
        super(string);
    }
}
