package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ErrorHandler {

    public List evaluateAndHandle(TerminalListOperation operation) {
        List result = null;
        try {
            result = operation.evaluate();
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            System.exit(2);
        }
        return result;
    }
}
