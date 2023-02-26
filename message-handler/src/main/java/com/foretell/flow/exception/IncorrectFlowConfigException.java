package com.foretell.flow.exception;

/**
 * The type Incorrect flow config exception.
 */
public class IncorrectFlowConfigException extends RuntimeException {
    /**
     * Instantiates a new Incorrect flow config exception.
     *
     * @param errorMessage the error message
     * @param err          the error
     */
    public IncorrectFlowConfigException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}