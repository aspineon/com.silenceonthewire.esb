package com.silenceonthewire.exceptions.api;

import org.apache.commons.lang3.StringUtils;

public class ExceptionFactory {

    public static Throwable getInstance(Fault fault) {
        if (fault == null)
            return null;
        return determineException(fault);
    }

    private static Throwable determineException(Fault fault) {
        String message = fault.errorMessage;

        if (StringUtils.isNotEmpty(message) && message.contains("Requires authentication")) {
            return new AuthenticationException(fault);
        }
        return new GenericException(fault);
    }

    public static class AuthenticationException extends RuntimeException {

        Fault fault;

        public AuthenticationException(Fault fault) {
            this.fault = fault;
        }
    }

    public static class GenericException extends RuntimeException {

        Fault fault;

        public GenericException(Fault fault) {
            this.fault = fault;
        }
    }
}

