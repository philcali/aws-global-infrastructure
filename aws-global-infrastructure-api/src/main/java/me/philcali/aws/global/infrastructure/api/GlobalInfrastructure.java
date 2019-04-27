package me.philcali.aws.global.infrastructure.api;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public interface GlobalInfrastructure extends RegionProvider, ServiceProvider {
    String version();

    static GlobalInfrastructure defaultInfrastructure(final ClassLoader loader) {
        return StreamSupport.stream(ServiceLoader.load(GlobalInfrastructure.class, loader).spliterator(), false)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Could not find a default GlobalInfrastructure impl on the classpath!"));
    }

    static GlobalInfrastructure defaultInfrastructure() {
        return defaultInfrastructure(ClassLoader.getSystemClassLoader());
    }
}
