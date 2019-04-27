package me.philcali.aws.global.infrastructure.api.exception;

public class ServiceNotFoundException extends RuntimeException {
    
    
    public ServiceNotFoundException(final String message) {
        super(message);
    }
}
