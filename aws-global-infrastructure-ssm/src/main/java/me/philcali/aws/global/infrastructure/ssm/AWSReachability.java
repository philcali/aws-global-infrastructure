package me.philcali.aws.global.infrastructure.ssm;

import static me.philcali.aws.global.infrastructure.ssm.Constants.REGIONS;
import static me.philcali.aws.global.infrastructure.ssm.Constants.SERVICES;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

import me.philcali.aws.global.infrastructure.api.Reachability;

public class AWSReachability implements Reachability, ParameterMixin {
    private static final String ENDPOINT = "endpoint";
    private static final String PROTOCOLS = "protocols";
    private final String regionId;
    private final String serviceId;
    private final AWSSimpleSystemsManagement ssm;

    public AWSReachability(
            final String regionId,
            final String serviceId,
            final AWSSimpleSystemsManagement ssm) {
        this.regionId = regionId;
        this.serviceId = serviceId;
        this.ssm = ssm;
    }

    @Override
    public String endpoint() {
        return parameter(namespace(
                REGIONS, regionId,
                SERVICES, serviceId,
                ENDPOINT), ssm).orElse("");
    }

    @Override
    public List<String> protocols() {
        return parameter(namespace(REGIONS, regionId, SERVICES, serviceId, PROTOCOLS), ssm)
                .map(value -> value.split("\\s*,\\s*"))
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList);
    }
}
