package me.philcali.aws.global.infrastructure.ssm;

import static me.philcali.aws.global.infrastructure.ssm.Constants.REGIONS;
import static me.philcali.aws.global.infrastructure.ssm.Constants.SERVICES;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

import me.philcali.aws.global.infrastructure.api.RegionalAvailability;
import me.philcali.aws.global.infrastructure.api.Service;

public class AWSService implements Service, ParameterMixin {
    private static final String LONG_NAME = "longName";
    private final String id;
    private AWSSimpleSystemsManagement ssm;

    public AWSService(final String id, final AWSSimpleSystemsManagement ssm) {
        this.id = id;
        this.ssm = ssm;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String longName() {
        return parameter(namespace(SERVICES, id, LONG_NAME), ssm)
                .orElse(id);
    }

    @Override
    public Optional<RegionalAvailability> region(final String regionId) {
        return parameter(namespace(SERVICES, id, REGIONS, regionId), ssm)
                .map(region -> new AWSRegionalAvailabiltiy(region, id, ssm));
    }

    @Override
    public Stream<RegionalAvailability> regions() {
        return stream(namespace(SERVICES, id, REGIONS), ssm)
                .map(param -> new AWSRegionalAvailabiltiy(param.getValue(), id, ssm));
    }

    @Override
    public boolean equals(final Object other) {
        if (Objects.isNull(other) || !(other instanceof Service)) {
            return false;
        }
        return Objects.equals(id, ((Service) other).id());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
