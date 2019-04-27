package me.philcali.aws.global.infrastructure.api;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Region {
    enum Partition implements Predicate<Region> {
        AWS("aws"), AWS_CN("aws-cn"), AWS_US_GOV("aws-us-gov");

        private String id;

        Partition(final String id) {
            this.id = id;
        }

        public String id() {
            return id;
        }

        @Override
        public boolean test(final Region region) {
            return region.partition().id().equals(id);
        }

        public static Partition fromPartition(final String partitionName) {
            for (final Partition partition : Partition.values()) {
                if (partition.id.equals(partitionName)) {
                    return partition;
                }
            }
            throw new IllegalArgumentException("Partition " + partitionName + " is not a valid AWS partition.");
        }
    }

    String id();

    String longName();

    Partition partition();

    String geolocationRegion();

    String geolocationCountry();

    String domain();

    Optional<RegionalAvailability> service(String serviceId);

    Stream<RegionalAvailability> services();
}
