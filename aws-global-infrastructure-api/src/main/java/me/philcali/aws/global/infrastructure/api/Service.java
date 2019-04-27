package me.philcali.aws.global.infrastructure.api;

import java.util.Optional;
import java.util.stream.Stream;

public interface Service {
    String id();

    String longName();

    Optional<RegionalAvailability> region(String regionId);

    Stream<RegionalAvailability> regions();
}
