package me.philcali.aws.global.infrastructure.api;

import java.util.Optional;
import java.util.stream.Stream;

import me.philcali.aws.global.infrastructure.api.exception.ServiceNotFoundException;

public interface ServiceProvider {
    Stream<Service> services();

    Service service(String serviceId) throws ServiceNotFoundException;
}
