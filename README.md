# AWS Global Infrastructure Library

Now that [AWS Regions and Service Endpoints][1] are available via Parameter Store,
You can do some pretty cool things!

## The Primitives

There are three basic primitives:

1. Region
1. Service
1. RegionalAvailability

From a `Region` you can list all services, and from a `Service`,
you can list all regions. In either case, you can find the
regional endpoint.

## Using the Global Infrastructure

``` java
final GlobalInfrastructure infra = GlobalInfrastructure.defaultInfrastructure();
```

## Listing all Regions

``` java
final Stream<Region> regions = infra.regions();
```

## Listing all Services

``` java
final Stream<Service> services = infra.services();
```

## Specific Service and Listing all Regions

``` java
final Service s3 = infra.service("s3");
s3.regions()
    .map(RegionAvailability::reachability)
    .map(Reachability::endpoint)
    .forEach(System.out::println);
```

## Specific Region and Listing all Services

``` java
final Region useast1 = infra.region("us-east-1");
useast1.services()
    .map(RegionAvailability::service)
    .map(Service::longName)
    .forEach(System.out::println);
```

## Explore

``` java
infra.regions().limit(5).forEach(region -> {
    System.out.println(region.longName() + " has " + region.services().count() + " services");
});
infra.services().limit(5).forEach(service -> {
    System.out.println(service.longName() + " is in " + service.regions().count() + " regions");
});
```

[1]: https://aws.amazon.com/blogs/aws/new-query-for-aws-regions-endpoints-and-more-using-aws-systems-manager-parameter-store/
