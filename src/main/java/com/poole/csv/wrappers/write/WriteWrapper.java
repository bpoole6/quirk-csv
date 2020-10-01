package com.poole.csv.wrappers.write;

public interface WriteWrapper<T> {
    String apply(T str);
}
