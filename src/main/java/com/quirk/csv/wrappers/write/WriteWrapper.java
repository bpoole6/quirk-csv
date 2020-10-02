package com.quirk.csv.wrappers.write;

public interface WriteWrapper<T> {
    String apply(T str);
}
