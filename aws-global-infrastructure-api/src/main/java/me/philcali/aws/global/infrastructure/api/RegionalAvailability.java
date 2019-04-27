package me.philcali.aws.global.infrastructure.api;

public interface RegionalAvailability {
    Region region();

    Service service();

    Reachability reachability();
}
