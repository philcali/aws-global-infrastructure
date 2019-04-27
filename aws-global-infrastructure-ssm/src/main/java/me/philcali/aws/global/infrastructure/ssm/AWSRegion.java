package me.philcali.aws.global.infrastructure.ssm;

import static me.philcali.aws.global.infrastructure.ssm.Constants.REGIONS;
import static me.philcali.aws.global.infrastructure.ssm.Constants.SERVICES;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

import me.philcali.aws.global.infrastructure.api.Region;
import me.philcali.aws.global.infrastructure.api.RegionalAvailability;

public class AWSRegion implements Region, ParameterMixin {
    private static final String DOMAIN = "domain";
    private static final String LONG_NAME = "longName";
    private static final String GEOLOCATION_COUNTRY = "geolocationCountry";
    private static final String GEOLOCATION_REGION = "geolocationRegion";
    private static final String PARTITION = "partition";
    private final String id;
    private final AWSSimpleSystemsManagement ssm;

    public AWSRegion(final String id, final AWSSimpleSystemsManagement ssm) {
        this.id = id;
        this.ssm = ssm;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String domain() {
        return parameter(namespace(REGIONS, id, DOMAIN), ssm).orElse("");
    }

    @Override
    public String geolocationCountry() {
        return parameter(namespace(REGIONS, id, GEOLOCATION_COUNTRY), ssm).orElse("");
    }

    @Override
    public String geolocationRegion() {
        return parameter(namespace(REGIONS, id, GEOLOCATION_REGION), ssm).orElse("");
    }

    @Override
    public String longName() {
        return parameter(namespace(REGIONS, id, LONG_NAME), ssm).orElse(id);
    }

    @Override
    public Partition partition() {
        return parameter(namespace(REGIONS, id, PARTITION), ssm)
                .map(Partition::fromPartition)
                .orElseThrow(() -> new IllegalArgumentException("Invalid partition found for " + id));
    }

    @Override
    public Stream<RegionalAvailability> services() {
        return stream(namespace(REGIONS, id, SERVICES), ssm)
                .map(parameter -> new AWSRegionalAvailabiltiy(id, parameter.getValue(), ssm));
    }

    @Override
    public Optional<RegionalAvailability> service(final String serviceId) {
        return parameter(namespace(REGIONS, id, SERVICES, serviceId), ssm)
                .map(service -> new AWSRegionalAvailabiltiy(id, service, ssm));
    }

    @Override
    public boolean equals(final Object other) {
        if (Objects.isNull(other) || !(other instanceof Region)) {
            return false;
        }
        return Objects.equals(id, ((Region) other).id());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
