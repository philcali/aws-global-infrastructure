package me.philcali.aws.global.infrastructure.api.exception;

public class RegionNotFoundException extends RuntimeException {
    
    
    public RegionNotFoundException(final String message) {
        super(message);
    }
}
