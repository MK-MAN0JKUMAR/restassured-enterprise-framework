package framework.core.service;

import java.util.Arrays;
import java.util.List;

public final class ServiceRegistry {

    private ServiceRegistry(){}

    public static List<String> all() {
        return Arrays.asList(
                "reqres",
                "petstore",
                "github"
        );
    }
}