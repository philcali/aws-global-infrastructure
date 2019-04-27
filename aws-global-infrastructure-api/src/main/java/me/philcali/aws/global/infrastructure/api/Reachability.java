package me.philcali.aws.global.infrastructure.api;

import java.util.List;

public interface Reachability {
    String endpoint();

    List<String> protocols();
}
