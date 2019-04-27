package me.philcali.aws.global.infrastructure.api;

import java.util.stream.Stream;

import me.philcali.aws.global.infrastructure.api.exception.RegionNotFoundException;

public interface RegionProvider {
    Stream<Region> regions();

    Region region(String regionId) throws RegionNotFoundException;

    default Stream<Region> regionsByCountry(String countryCode) {
        return regions().filter(region -> region.geolocationCountry().equalsIgnoreCase(countryCode));
    }
}
