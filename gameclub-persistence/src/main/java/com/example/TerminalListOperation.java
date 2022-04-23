package com.example;

import java.util.List;

@FunctionalInterface
public interface TerminalListOperation<T> {

    List<T> evaluate() throws Exception;
}
