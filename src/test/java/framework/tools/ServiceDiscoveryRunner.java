package framework.tools;

import framework.core.service.ServiceRegistry;

public class ServiceDiscoveryRunner {

    public static void main(String[] args) {

        ServiceRegistry.discoverServices()
                .forEach(System.out::println);

    }
}