package me.philcali.aws.global.infrastructure.ssm;

import static me.philcali.aws.global.infrastructure.ssm.Constants.REGIONS;
import static me.philcali.aws.global.infrastructure.ssm.Constants.SERVICES;
import static me.philcali.aws.global.infrastructure.ssm.Constants.VERSION;

import java.util.Optional;
import java.util.stream.Stream;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.google.auto.service.AutoService;

import me.philcali.aws.global.infrastructure.api.GlobalInfrastructure;
import me.philcali.aws.global.infrastructure.api.Region;
import me.philcali.aws.global.infrastructure.api.Service;
import me.philcali.aws.global.infrastructure.api.exception.RegionNotFoundException;
import me.philcali.aws.global.infrastructure.api.exception.ServiceNotFoundException;

@AutoService(GlobalInfrastructure.class)
public class AWSGlobalInfrastructure implements GlobalInfrastructure, ParameterMixin {
    private AWSSimpleSystemsManagement ssm;

    public AWSGlobalInfrastructure() {
        this(AWSSimpleSystemsManagementClientBuilder.defaultClient());
    }

    public AWSGlobalInfrastructure(final AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
    }

    @Override
    public String version() {
        return parameter(namespace(VERSION), ssm).get();
    }

    @Override
    public Stream<Region> regions() {
        return stream(namespace(REGIONS), ssm)
                .map(parameter -> new AWSRegion(parameter.getValue(), ssm));
    }

    @Override
    public Region region(final String regionId) throws RegionNotFoundException {
        return parameter(namespace(REGIONS, regionId.toLowerCase()), ssm)
                .map(param -> new AWSRegion(param, ssm))
                .orElseThrow(() -> new RegionNotFoundException("Could not find region: " + regionId.toLowerCase()));
    }

    @Override
    public Service service(final String serviceId) throws ServiceNotFoundException {
        return parameter(namespace(SERVICES, serviceId.toLowerCase()), ssm)
                .map(param -> new AWSService(param, ssm))
                .orElseThrow(() -> new ServiceNotFoundException("Could not find service: " + serviceId.toLowerCase()));
    }

    @Override
    public Stream<Service> services() {
        return stream(namespace(SERVICES), ssm)
                .map(parameter -> new AWSService(parameter.getValue(), ssm));
    }
}
