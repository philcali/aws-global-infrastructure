package me.philcali.aws.global.infrastructure.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

import me.philcali.aws.global.infrastructure.api.Reachability;
import me.philcali.aws.global.infrastructure.api.Region;
import me.philcali.aws.global.infrastructure.api.RegionalAvailability;
import me.philcali.aws.global.infrastructure.api.Service;

public class AWSRegionalAvailabiltiy implements RegionalAvailability {
    private final String regionId;
    private final String serviceId;
    private final AWSSimpleSystemsManagement ssm;

    public AWSRegionalAvailabiltiy(
            final String regionId,
            final String serviceId,
            final AWSSimpleSystemsManagement ssm) {
        this.regionId = regionId;
        this.serviceId = serviceId;
        this.ssm = ssm;
    }

    @Override
    public Reachability reachability() {
        return new AWSReachability(regionId, serviceId, ssm);
    }

    @Override
    public Region region() {
        return new AWSRegion(regionId, ssm);
    }

    @Override
    public Service service() {
        return new AWSService(serviceId, ssm);
    }
}
